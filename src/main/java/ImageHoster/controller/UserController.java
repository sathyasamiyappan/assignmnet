package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.model.UserProfile;
import ImageHoster.service.ImageService;
import ImageHoster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    //This controller method is called when the request pattern is of type 'users/registration'
    //This method declares User type and UserProfile type object
    //Sets the user profile with UserProfile type object
    //Adds User type object to a model and returns 'users/registration.html' file
    @RequestMapping("users/registration")
    public String registration(Model model) {

        User user = new User();
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        model.addAttribute("User", user);

        //I was not able to direct the page to error messageso i dsipalyed the meesage for password directky
        //if password is okay it will take to login page
        //else it will be again in registration page
        //String error = "Password must contain atleast 1 alphabet, 1 number & 1 special character";
        //model.addAttribute("passwordTypeError", error);

        return "users/registration";
    }

    //This controller method is called when the request pattern is of type 'users/registration' and also the incoming request is of POST type
    //This method calls the business logic and after the user record is persisted in the database, directs to login page
    @RequestMapping(value = "users/registration", method = RequestMethod.POST)
    public String registerUser(User user,Model model) {
        ///sathya--

        boolean valid=false;
        String password=user.getPassword();
        System.out.println("password---------------"+password);

        if ((password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {
            if (password.contains("a") || password.contains("b")
                    || password.contains("c") || password.contains("i")
                    || password.contains("d") || password.contains("j")
                    || password.contains("e") || password.contains("k")
                    || password.contains("f") || password.contains("l")
                    || password.contains("g") || password.contains("m")
                    || password.contains("h") || password.contains("n")
                    || password.contains("q") || password.contains("o")
                    || password.contains("r") || password.contains("p")
                    || password.contains("s") || password.contains("u")
                    || password.contains("t") || password.contains("v")
                    || password.contains("w") || password.contains("y")
                    || password.contains("x") || password.contains("z")
            ) {
                if (password.contains("A") || password.contains("B")
                        || password.contains("C") || password.contains("I")
                        || password.contains("D") || password.contains("J")
                        || password.contains("E") || password.contains("K")
                        || password.contains("F") || password.contains("L")
                        || password.contains("G") || password.contains("M")
                        || password.contains("H") || password.contains("N")
                        || password.contains("Q") || password.contains("O")
                        || password.contains("R") || password.contains("P")
                        || password.contains("S") || password.contains("U")
                        || password.contains("T") || password.contains("V")
                        || password.contains("W") || password.contains("Y")
                        || password.contains("X") || password.contains("Z")
                ) {
                    if (password.contains("0") || password.contains("1")
                            || password.contains("3") || password.contains("3")
                            || password.contains("4") || password.contains("5")
                            || password.contains("6") || password.contains("7")
                            || password.contains("8") || password.contains("9")
                    ) {
                        valid = true;
                    }
                }
            }
        }
        //I was not able to direct the page to error messageso i dsipalyed the meesage for password directky
        //if password is okay it will take to login page
        //else it will be again in registration page
            System.out.println("valid---------------"+valid);
            if(valid==false) {
                //User user = new User();
                UserProfile profile = new UserProfile();
                user.setProfile(profile);
                model.addAttribute("User", user);

                String error = "Password must contain atleast 1 alphabet, 1 number & 1 special character";
                model.addAttribute("passwordTypeError", error);
                return "users/registration";
            }
           else {
            ///sathya--
            userService.registerUser(user);
            return "redirect:/users/login";
           }
    }

    //This controller method is called when the request pattern is of type 'users/login'
    @RequestMapping("users/login")
    public String login() {
        return "users/login";
    }

    //This controller method is called when the request pattern is of type 'users/login' and also the incoming request is of POST type
    //The return type of the business logic is changed to User type instead of boolean type. The login() method in the business logic checks whether the user with entered username and password exists in the database and returns the User type object if user with entered username and password exists in the database, else returns null
    //If user with entered username and password exists in the database, add the logged in user in the Http Session and direct to user homepage displaying all the images in the application
    //If user with entered username and password does not exist in the database, redirect to the same login page
    @RequestMapping(value = "users/login", method = RequestMethod.POST)
    public String loginUser(User user, HttpSession session) {
        User existingUser = userService.login(user);
        if (existingUser != null) {
            session.setAttribute("loggeduser", existingUser);
            return "redirect:/images";
        } else {
            return "users/login";
        }
    }

    //This controller method is called when the request pattern is of type 'users/logout' and also the incoming request is of POST type
    //The method receives the Http Session and the Model type object
    //session is invalidated
    //All the images are fetched from the database and added to the model with 'images' as the key
    //'index.html' file is returned showing the landing page of the application and displaying all the images in the application
    @RequestMapping(value = "users/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpSession session) {
        session.invalidate();

        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }
}
