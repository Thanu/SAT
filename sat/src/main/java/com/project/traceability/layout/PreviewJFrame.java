package com.project.traceability.layout;

public class PreviewJFrame extends APreviewFrame {

    public PreviewJFrame()
    {
        super();
        this.layoutUpdaterFct = new LayoutUpdaterFct(this);        
        layoutUpdaterFct.createLayoutRunnable();
    }

    private LayoutUpdaterFct layoutUpdaterFct;

    public void Update()
    {
        previewController.refreshPreview();
        applet.redraw();
    }

    public void script() {
        AddGraph();
        Prepare();
        Display(applet);

//        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
//        scheduler.scheduleAtFixedRate(layoutUpdaterFct.createLayoutRunnable(), 0, 100, TimeUnit.MICROSECONDS);
//        scheduler.scheduleAtFixedRate(layoutUpdaterFct.fillGraphRunnable(), 0, 5000, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        PreviewJFrame previewJFrame = new PreviewJFrame();
        previewJFrame.script();
    }
}
