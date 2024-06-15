package com.smartcontactmanager.smartcontactmanager.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontactmanager.smartcontactmanager.Dao.ContactRepository;
import com.smartcontactmanager.smartcontactmanager.Dao.UserRepository;
import com.smartcontactmanager.smartcontactmanager.Helper.Message;
import com.smartcontactmanager.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.smartcontactmanager.entities.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

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
        return "normaluser/user_dashboard";
    }

    // open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {

        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());

        return "normaluser/add_contact_form";

    }

    // processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
            @RequestParam("profileImage") MultipartFile file, Principal principal,
            HttpSession session) {

        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            // processing and uploading file
            if (file.isEmpty()) {
                System.out.println("File is empty");
                contact.setImage("Contact_Icon.png");

            } else {

                contact.setImage(file.getOriginalFilename());
                File savefile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(savefile.getAbsolutePath() + File.separator +
                        file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image is Uploaded");

            }
            user.getContact().add(contact);
            contact.setUser(user);
            this.userRepository.save(user);
            System.out.println("DATA " + contact);
            System.out.println("Added to database");
            // message success
            session.setAttribute("message", new Message("Your Contact is added !! Add  more.. ", "success"));

        } catch (Exception e) {

            System.out.println("ERROR" + e.getMessage());
            e.printStackTrace();

            // message error
            session.setAttribute("message", new Message("Something Went Wrong !! try again..", "danger"));

        }

        return "normaluser/add_contact_form";

    }

    // Show Contact Handler
    // per page =5[n]
    // current page =0[n]
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Show User Contacts");
        String userName = principal.getName();

        User user = this.userRepository.getUserByUserName(userName);

        // currentPage-page
        // Contact Per page -5
        Pageable pageable = PageRequest.of(page, 3);

        Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "normaluser/show_contacts";
    }

    // showing particular contact details
    @GetMapping("/{cid}/contacts")
    public String showContactDetail(@PathVariable("cid") Integer cid, Model model, Principal principal) {
        System.out.println("CID" + cid);

        Optional<Contact> cOptional = this.contactRepository.findById(cid);
        Contact contact = cOptional.get();

        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        if (user.getId() == contact.getUser().getId()) {

            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "normaluser/contact_detail";
    }

    // delete contact handler
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) {

        Contact contact = this.contactRepository.findById(cid).get();

        contact.setUser(null);

        // check
        this.contactRepository.delete(contact);
        session.setAttribute("message", new Message("Contact deleted Succesfully... ", "success"));

        return "redirect:/user/show-contacts/0";

    }
    
    @GetMapping("/update-Contact/{cid}")
    public String updateContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) {

        Contact contact = this.contactRepository.findById(cid).get();

        boolean showListPage= true;
	     if (contact == null) {
	         // Handle the case where the user with the provided ID is not found
	         // You can redirect to an error page or display an error message
	         return "/normaluser/show_contacts";
	     }
	     model.addAttribute("showListPage" ,showListPage);
	     // Add the user object to the model for displaying the update form
	     model.addAttribute("contact", contact);
	     return "/normaluser/update_form"; // Return the name of your Thymeleaf template for the update user page
	 }

    @PostMapping("/update-Contact/{cid}")
    public String updateForm(@ModelAttribute("contact") Contact contact,
                             Model model,
                             @PathVariable("cid") Integer cid) {
        Contact existingContact = this.contactRepository.findById(cid).orElse(null);
        if (existingContact != null) {
            existingContact.setName(contact.getName());
            existingContact.setEmail(contact.getEmail());
            // Update other fields as needed
            contactRepository.save(existingContact);
            model.addAttribute("contact", existingContact);
            model.addAttribute("cid", cid);
        }
        return "redirect:/user/show-contacts/0";
    }
    @GetMapping("/update/{cid}")
    public String updateContacts(@PathVariable("cid") Integer cid, Model model, HttpSession session) {

        Contact contact = this.contactRepository.findById(cid).get();

        boolean showListPage= false;
	     if (contact == null) {
	         // Handle the case where the user with the provided ID is not found
	         // You can redirect to an error page or display an error message
	         return "/normaluser/show_contacts";
	     }
	     model.addAttribute("showListPage" ,showListPage);
	     // Add the user object to the model for displaying the update form
	     model.addAttribute("contact", contact);
	     return "/normaluser/update_form"; // Return the name of your Thymeleaf template for the update user page
	 }
    
    
    @PostMapping("/update/{cid}")
    public String updateForms(@ModelAttribute("contact") Contact contact,
                             Model model,
                             @PathVariable("cid") Integer cid) {
    	
        Contact existingContact = this.contactRepository.findById(cid).orElse(null);
        if (existingContact != null) {
            existingContact.setName(contact.getName());
            existingContact.setEmail(contact.getEmail());
            // Update other fields as needed
            contactRepository.save(existingContact);
            model.addAttribute("contact", existingContact);
            model.addAttribute("cid", cid);
           
            
        }
        return "redirect:/user/{cid}/contacts";
    }

}
