import AppKit

final class MainWindowController: NSWindowController {
    convenience init() {
        let contentViewController = MainViewController()
        let window = NSWindow(
            contentRect: NSRect(x: 0, y: 0, width: 760, height: 470),
            styleMask: [.titled, .closable, .miniaturizable],
            backing: .buffered,
            defer: false
        )

        window.title = "SineSlider"
        window.contentViewController = contentViewController
        window.center()
        window.isReleasedWhenClosed = false

        self.init(window: window)
    }
}
