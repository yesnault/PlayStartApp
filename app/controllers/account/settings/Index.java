package controllers.account.settings;

import controllers.Secured;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Index Settings page.
 * <p/>
 * User: yesnault
 * Date: 15/05/12
 */
@Security.Authenticated(Secured.class)
public class Index extends Controller {

    /**
     * Main page settings
     *
     * @return index settings
     */
    public Result index() {
        return new Password().index();
    }
}
