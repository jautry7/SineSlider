import AppKit

final class MainViewController: NSViewController {
    private let colorFactory = ColorFactory()
    private lazy var graphView = GraphView(colorFactory: colorFactory)
    private let channelSelector = NSSegmentedControl(
        labels: ColorChannel.allCases.map(\.name),
        trackingMode: .selectOne,
        target: nil,
        action: nil
    )
    private let controlsStack = NSStackView()
    private let sampleStack = NSStackView()

    private var selectedChannel: ColorChannel = .red
    private var sliders: [CurveTransform: NSSlider] = [:]
    private var percentageLabels: [CurveTransform: NSTextField] = [:]
    private var sampleValueLabels: [NSTextField] = []

    override func loadView() {
        view = NSView(frame: NSRect(x: 0, y: 0, width: 760, height: 470))
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        configureLayout()
        selectChannel(nil)
        updateSample(at: 0)
    }

    private func configureLayout() {
        graphView.translatesAutoresizingMaskIntoConstraints = false
        graphView.onCursorChange = { [weak self] x in
            self?.updateSample(at: x)
        }
        view.addSubview(graphView)

        let inspector = NSVisualEffectView()
        inspector.material = .sidebar
        inspector.blendingMode = .withinWindow
        inspector.state = .active
        inspector.wantsLayer = true
        inspector.layer?.cornerRadius = 10
        inspector.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(inspector)

        channelSelector.selectedSegment = ColorChannel.red.rawValue
        channelSelector.target = self
        channelSelector.action = #selector(selectChannel(_:))
        channelSelector.translatesAutoresizingMaskIntoConstraints = false
        inspector.addSubview(channelSelector)

        controlsStack.orientation = .vertical
        controlsStack.alignment = .leading
        controlsStack.spacing = 18
        controlsStack.translatesAutoresizingMaskIntoConstraints = false
        inspector.addSubview(controlsStack)

        for transform in CurveTransform.allCases {
            controlsStack.addArrangedSubview(makeSliderRow(for: transform))
        }

        configureSampleReadout()

        NSLayoutConstraint.activate([
            graphView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 18),
            graphView.topAnchor.constraint(equalTo: view.topAnchor, constant: 18),
            graphView.widthAnchor.constraint(equalToConstant: 330),
            graphView.bottomAnchor.constraint(equalTo: sampleStack.topAnchor, constant: -6),

            inspector.leadingAnchor.constraint(equalTo: graphView.trailingAnchor, constant: 20),
            inspector.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
            inspector.topAnchor.constraint(equalTo: view.topAnchor, constant: 24),
            inspector.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -24),

            channelSelector.topAnchor.constraint(equalTo: inspector.topAnchor, constant: 20),
            channelSelector.leadingAnchor.constraint(equalTo: inspector.leadingAnchor, constant: 20),
            channelSelector.trailingAnchor.constraint(equalTo: inspector.trailingAnchor, constant: -20),

            controlsStack.topAnchor.constraint(equalTo: channelSelector.bottomAnchor, constant: 26),
            controlsStack.leadingAnchor.constraint(equalTo: inspector.leadingAnchor, constant: 22),
            controlsStack.trailingAnchor.constraint(equalTo: inspector.trailingAnchor, constant: -22),

            sampleStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 30),
            sampleStack.trailingAnchor.constraint(equalTo: inspector.leadingAnchor, constant: -10),
            sampleStack.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -25)
        ])
    }

    private func configureSampleReadout() {
        sampleStack.orientation = .horizontal
        sampleStack.alignment = .centerY
        sampleStack.distribution = .fillEqually
        sampleStack.spacing = 8
        sampleStack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(sampleStack)

        for name in ["x", "R", "G", "B"] {
            let valueLabel = NSTextField(labelWithString: "0")
            valueLabel.alignment = .center
            valueLabel.font = .monospacedDigitSystemFont(ofSize: 13, weight: .medium)
            valueLabel.wantsLayer = true
            valueLabel.layer?.backgroundColor = NSColor.controlBackgroundColor.cgColor
            valueLabel.layer?.cornerRadius = 5
            valueLabel.layer?.borderColor = NSColor.separatorColor.cgColor
            valueLabel.layer?.borderWidth = 1
            valueLabel.heightAnchor.constraint(equalToConstant: 28).isActive = true

            let nameLabel = NSTextField(labelWithString: name)
            nameLabel.textColor = .secondaryLabelColor
            nameLabel.alignment = .right

            let pair = NSStackView(views: [nameLabel, valueLabel])
            pair.orientation = .horizontal
            pair.alignment = .centerY
            pair.spacing = 5
            sampleStack.addArrangedSubview(pair)
            sampleValueLabels.append(valueLabel)
        }
    }

    private func makeSliderRow(for transform: CurveTransform) -> NSView {
        let titleLabel = NSTextField(labelWithString: transform.title)
        titleLabel.font = .systemFont(ofSize: 13, weight: .medium)

        let percentageLabel = NSTextField(labelWithString: "0%")
        percentageLabel.font = .monospacedDigitSystemFont(ofSize: 12, weight: .regular)
        percentageLabel.textColor = .secondaryLabelColor
        percentageLabel.alignment = .right
        percentageLabel.widthAnchor.constraint(equalToConstant: 44).isActive = true
        percentageLabels[transform] = percentageLabel

        let heading = NSStackView(views: [titleLabel, percentageLabel])
        heading.orientation = .horizontal
        heading.distribution = .fill
        heading.alignment = .centerY

        let slider = NSSlider(
            value: 0,
            minValue: 0,
            maxValue: 100,
            target: self,
            action: #selector(sliderChanged(_:))
        )
        slider.isContinuous = true
        slider.tag = transform.rawValue
        slider.widthAnchor.constraint(greaterThanOrEqualToConstant: 300).isActive = true
        sliders[transform] = slider

        let row = NSStackView(views: [heading, slider])
        row.orientation = .vertical
        row.alignment = .leading
        row.spacing = 7
        heading.widthAnchor.constraint(equalTo: row.widthAnchor).isActive = true
        slider.widthAnchor.constraint(equalTo: row.widthAnchor).isActive = true

        return row
    }

    @objc private func selectChannel(_ sender: NSSegmentedControl?) {
        selectedChannel = ColorChannel(rawValue: channelSelector.selectedSegment) ?? .red

        for transform in CurveTransform.allCases {
            let percentage = Int(
                (colorFactory.factor(for: selectedChannel, transform: transform) * 100).rounded()
            )
            sliders[transform]?.integerValue = percentage
            percentageLabels[transform]?.stringValue = "\(percentage)%"
        }
    }

    @objc private func sliderChanged(_ sender: NSSlider) {
        guard let transform = CurveTransform(rawValue: sender.tag) else {
            return
        }

        colorFactory.setFactor(
            sender.doubleValue / 100.0,
            for: selectedChannel,
            transform: transform
        )
        percentageLabels[transform]?.stringValue = "\(sender.integerValue)%"
        graphView.needsDisplay = true
        updateSample(at: graphView.cursorX)
    }

    private func updateSample(at x: Int) {
        guard sampleValueLabels.count == 4 else {
            return
        }

        sampleValueLabels[0].stringValue = "\(x)"
        for channel in ColorChannel.allCases {
            sampleValueLabels[channel.rawValue + 1].stringValue = "\(colorFactory.value(for: channel, at: x))"
        }
    }
}
