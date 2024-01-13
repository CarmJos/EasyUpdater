package cc.carm.app.easyupdater.action;

public class ActionResult {

    protected final int success;
    protected final int failed;

    public ActionResult(int success, int failed) {
        this.success = success;
        this.failed = failed;
    }

    public int success() {
        return success;
    }

    public int failed() {
        return failed;
    }
    
}