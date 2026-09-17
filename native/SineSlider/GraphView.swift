import AppKit

final class GraphView: NSView {
    let colorFactory: ColorFactory
    var cursorX = 0
    var onCursorChange: ((Int) -> Void)?

    private var cursorTrackingArea: NSTrackingArea?
    private let plotSize: CGFloat = 256
    private let plotOrigin = CGPoint(x: 48, y: 88)

    init(colorFactory: ColorFactory) {
        self.colorFactory = colorFactory
        super.init(frame: .zero)
        wantsLayer = true
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func updateTrackingAreas() {
        if let cursorTrackingArea {
            removeTrackingArea(cursorTrackingArea)
        }

        let trackingArea = NSTrackingArea(
            rect: bounds,
            options: [.activeInKeyWindow, .mouseMoved, .mouseEnteredAndExited, .inVisibleRect],
            owner: self
        )
        addTrackingArea(trackingArea)
        cursorTrackingArea = trackingArea

        super.updateTrackingAreas()
    }

    override func mouseMoved(with event: NSEvent) {
        updateCursor(with: event)
    }

    override func mouseDragged(with event: NSEvent) {
        updateCursor(with: event)
    }

    override func mouseDown(with event: NSEvent) {
        updateCursor(with: event)
    }

    override func draw(_ dirtyRect: NSRect) {
        super.draw(dirtyRect)

        NSColor.controlBackgroundColor.setFill()
        bounds.fill()

        let plotRect = NSRect(
            origin: plotOrigin,
            size: NSSize(width: plotSize, height: plotSize)
        )
        NSColor.textBackgroundColor.setFill()
        plotRect.fill()

        drawGrid(in: plotRect)
        drawCurves(in: plotRect)
        drawCursor(in: plotRect)
        drawGradient(below: plotRect)
        drawAxisLabels(around: plotRect)
    }

    private func updateCursor(with event: NSEvent) {
        let location = convert(event.locationInWindow, from: nil)
        cursorX = min(255, max(0, Int(location.x - plotOrigin.x)))
        onCursorChange?(cursorX)
        needsDisplay = true
    }

    private func drawGrid(in rect: NSRect) {
        let gridPath = NSBezierPath()
        gridPath.lineWidth = 1

        for step in 0...8 {
            let y = rect.minY + CGFloat(step) * 32 + 0.5
            gridPath.move(to: CGPoint(x: rect.minX, y: y))
            gridPath.line(to: CGPoint(x: rect.maxX, y: y))
        }

        NSColor.separatorColor.setStroke()
        gridPath.stroke()

        NSColor.quaternaryLabelColor.setStroke()
        NSBezierPath(rect: rect).stroke()
    }

    private func drawCurves(in rect: NSRect) {
        for channel in ColorChannel.allCases {
            let curve = NSBezierPath()
            curve.lineWidth = 1.75

            for x in 0..<256 {
                let point = CGPoint(
                    x: rect.minX + CGFloat(x),
                    y: rect.minY + CGFloat(colorFactory.value(for: channel, at: x))
                )
                x == 0 ? curve.move(to: point) : curve.line(to: point)
            }

            channel.displayColor.setStroke()
            curve.stroke()
        }
    }

    private func drawCursor(in rect: NSRect) {
        let x = rect.minX + CGFloat(cursorX) + 0.5
        let cursorLine = NSBezierPath()
        cursorLine.move(to: CGPoint(x: x, y: rect.minY))
        cursorLine.line(to: CGPoint(x: x, y: rect.maxY))
        cursorLine.lineWidth = 1
        NSColor.labelColor.withAlphaComponent(0.72).setStroke()
        cursorLine.stroke()

        for channel in ColorChannel.allCases {
            let y = rect.minY + CGFloat(colorFactory.value(for: channel, at: cursorX))
            let marker = NSBezierPath(
                ovalIn: NSRect(x: x - 4.5, y: y - 4.5, width: 9, height: 9)
            )
            NSColor.windowBackgroundColor.setFill()
            marker.fill()
            channel.displayColor.setStroke()
            marker.lineWidth = 2
            marker.stroke()
        }
    }

    private func drawGradient(below rect: NSRect) {
        let gradientRect = NSRect(x: rect.minX, y: 48, width: rect.width, height: 24)

        for x in 0..<256 {
            colorFactory.color(at: x).setFill()
            NSRect(
                x: gradientRect.minX + CGFloat(x),
                y: gradientRect.minY,
                width: 1,
                height: gradientRect.height
            ).fill()
        }

        NSColor.separatorColor.setStroke()
        NSBezierPath(roundedRect: gradientRect, xRadius: 4, yRadius: 4).stroke()
    }

    private func drawAxisLabels(around rect: NSRect) {
        let attributes: [NSAttributedString.Key: Any] = [
            .font: NSFont.monospacedDigitSystemFont(ofSize: 11, weight: .regular),
            .foregroundColor: NSColor.secondaryLabelColor
        ]

        for value in stride(from: 0, through: 192, by: 64) {
            drawAxisLabel(value, y: rect.minY + CGFloat(value), rect: rect, attributes: attributes)
        }
        drawAxisLabel(255, y: rect.maxY, rect: rect, attributes: attributes)
    }

    private func drawAxisLabel(
        _ value: Int,
        y: CGFloat,
        rect: NSRect,
        attributes: [NSAttributedString.Key: Any]
    ) {
        let label = "\(value)" as NSString
        let labelSize = label.size(withAttributes: attributes)
        label.draw(
            at: CGPoint(x: rect.minX - labelSize.width - 8, y: y - labelSize.height / 2),
            withAttributes: attributes
        )
    }
}
