package controllers.account;

import controllers.Secured;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.account.settings.index;

/**
 * User: yesnault
 * Date: 15/05/12
 */
@Security.Authenticated(Secured.class)
public class Settings extends Controller {

    public static Result index() {

        return ok(index.render(User.findByEmail(request().username())));
    }
}
