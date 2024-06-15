package com.smartcontactmanager.smartcontactmanager.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartcontactmanager.smartcontactmanager.Dao.ContactRepository;
import com.smartcontactmanager.smartcontactmanager.Dao.UserRepository;
import com.smartcontactmanager.smartcontactmanager.entities.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	 @Autowired
	    UserRepository userRepository;
	    @Autowired
	    ContactRepository contactRepository;

	    // method to adding common data to responce
	    @ModelAttribute
	    public void addCommmonData(Model model, Principal principal) {

	        String userName = principal.getName();
	        System.out.println("USERNAME" + userName);

	        User user = userRepository.getUserByUserName(userName);
	        // get the user using username(Email)

	        System.out.println("USER" + user);
	        model.addAttribute("user", user);
	    }

	    // dashboard home
	    @RequestMapping("/index")
	    public String dashboard(Model model, Principal principal) {

	        model.addAttribute("title", "User Dashboard");
	        return "adminUser/abc";
	    }

}
