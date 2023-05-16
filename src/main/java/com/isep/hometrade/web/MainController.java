package com.isep.hometrade.web;

import com.isep.hometrade.business.*;
import com.isep.hometrade.map.*;
import com.isep.hometrade.service.*;
import com.isep.hometrade.util.FileUpload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    private final UserService userService;
    private final AddressService addressService;
    private final HousingService housingService;
    private final PhotoService photoService;
    private final ServiceService serviceService;
    private final ConstraintService constraintService;



    @Autowired
    public MainController(UserService userService, AddressService addressService, HousingService housingService, PhotoService photoService, ServiceService serviceService, ConstraintService constraintService) {
        this.userService = userService;
        this.addressService = addressService;
        this.housingService = housingService;
        this.photoService = photoService;
        this.serviceService = serviceService;
        this.constraintService = constraintService;
    }

    @GetMapping("/")
    public String welcome(Model model) {
        Set<HousingEntity> housingEntities = housingService.find5LastHousings();
        model.addAttribute("housingEntities", housingEntities);
        return "welcome";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        UserEntity existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null) {
            result.rejectValue("email", null, "L'adresse e-mail saisie est déjà associée à un compte !");
        }
        if (result.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "registration";
        }
        userService.saveUser(userDto);
        return "redirect:/registration?success";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        UserEntity userEntity = userService.findUserByEmail(authentication.getName());
        Set<HousingEntity> housingEntities = housingService.findHousingsByUser(userEntity);
        model.addAttribute("userEntity", userEntity);
        model.addAttribute("housingEntities", housingEntities);
        return "profile";
    }

    @GetMapping("/add-housing")
    public String addHousing(Model model) {
        AddressDto addressDto = new AddressDto();
        model.addAttribute("addressDto", addressDto);
        return "add-housing-page-1";
    }

    @PostMapping("/add-housing/page-1")
    public String processAddHousingPage1(@Valid @ModelAttribute("addressDto") AddressDto addressDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("addressDto", addressDto);
            return "add-housing-page-1";
        }
        HousingDto housingDto = new HousingDto();
        session.setAttribute("addressDto", addressDto);
        model.addAttribute("housingDto", housingDto);
        return "add-housing-page-2";
    }

    @PostMapping("/add-housing/page-2")
    public String processAddHousingPage2(@Valid @ModelAttribute("housingDto") HousingDto housingDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("housingDto", housingDto);
            return "add-housing-page-2";
        }
        String uuid = UUID.randomUUID().toString();
        String prefixFromLocal = "C:/wamp64/www/images/housings/housing-" + uuid + "/";
        String prefixFromServer = "http://localhost/images/housings/housing-" + uuid + "/";
        PhotoDto[] photoDtoSToBeAdd = new PhotoDto[0];
        PhotoDto[] photoDtoS = new PhotoDto[0];
        session.setAttribute("housingDto", housingDto);
        session.setAttribute("uuid", uuid);
        session.setAttribute("prefixFromLocal", prefixFromLocal);
        session.setAttribute("prefixFromServer", prefixFromServer);
        session.setAttribute("photoDtoSToBeAdd", photoDtoSToBeAdd);
        session.setAttribute("photoDtoS", photoDtoS);
        model.addAttribute("photoDtoS", photoDtoS);
        return "add-housing-page-3";
    }

    @PostMapping("/add-housing/page-3/upload")
    public String processUploadAddHousingPage3(@RequestParam("file") MultipartFile file, Model model, HttpSession session) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Veuillez choisir un fichier !");
            return "add-housing-page-3";
        }
        String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
        try {
            FileUpload.saveFile(prefixFromLocal, file);
        } catch (IOException e) {
            model.addAttribute("error", "Il y a eu une erreur !");
            return "add-housing-page-3";
        }
        PhotoDto[] photoDtoSToBeAdd = (PhotoDto[]) session.getAttribute("photoDtoSToBeAdd");
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> temPhotoDtoSToBeAdd = new ArrayList<>(Arrays.asList(photoDtoSToBeAdd));
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        String prefixFromServer = (String) session.getAttribute("prefixFromServer");
        String fileName = file.getOriginalFilename();
        PhotoDto photoDto = new PhotoDto();
        photoDto.setName(fileName);
        photoDto.setPath(prefixFromServer + fileName);
        temPhotoDtoSToBeAdd.add(photoDto);
        tempPhotoDtoS.add(photoDto);
        photoDtoSToBeAdd = temPhotoDtoSToBeAdd.toArray(new PhotoDto[0]);
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        session.setAttribute("photoDtoSToBeAdd", photoDtoSToBeAdd);
        session.setAttribute("photoDtoS", photoDtoS);
        model.addAttribute("photoDtoS", photoDtoS);
        return "add-housing-page-3";
    }

    @PostMapping(path = "/add-housing/page-3/delete")
    public String processDeleteAddHousingPage3(@RequestParam("name") String name, Model model, HttpSession session) {
        PhotoDto[] photoDtoSToBeAdd = (PhotoDto[]) session.getAttribute("photoDtoSToBeAdd");
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> temphotoDtoSToBeAdd = new ArrayList<>(Arrays.asList(photoDtoSToBeAdd));
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        int i = 0;
        while (i < temphotoDtoSToBeAdd.size()) {
            String tempPhotoName = temphotoDtoSToBeAdd.get(i).getName();
            if (Objects.equals(tempPhotoName, name)) {
                temphotoDtoSToBeAdd.remove(i);
                String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
                try {
                    FileUpload.deleteFile(prefixFromLocal + tempPhotoName);
                } catch (IOException e) {
                    model.addAttribute("error", "Il y a eu une erreur !");
                    return "add-housing-page-3";
                }
            } else {
                i++;
            }
        }
        tempPhotoDtoS.removeIf(tempPhotoDto -> Objects.equals(tempPhotoDto.getName(), name));
        photoDtoSToBeAdd = temphotoDtoSToBeAdd.toArray(new PhotoDto[0]);
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        session.setAttribute("photoDtoSToBeAdd", photoDtoSToBeAdd);
        session.setAttribute("photoDtoS", photoDtoS);
        model.addAttribute("photoDtoS", photoDtoS);
        return "add-housing-page-3";
    }

    @PostMapping("/add-housing/page-3")
    public String processAddHousingPage3(Model model, HttpSession session) {
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        if (tempPhotoDtoS.isEmpty()) {
            model.addAttribute("error", "Veuillez ajouter au moins une photo !");
            return "add-housing-page-3";
        }
        ServiceDto[] serviceDtoSToBeAdd = new ServiceDto[0];
        ServiceDto[] serviceDtoS = new ServiceDto[0];
        session.setAttribute("serviceDtoSToBeAdd", serviceDtoSToBeAdd);
        session.setAttribute("serviceDtoS", serviceDtoS);
        model.addAttribute("serviceDtoS", serviceDtoS);
        return "add-housing-page-4";
    }

    @PostMapping("/add-housing/page-4/upload")
    public String processUploadAddHousingPage4(@RequestParam("name") String name, Model model, HttpSession session) {
        if (name.isEmpty()) {
            model.addAttribute("error", "Veuillez saisir un nom !");
            return "add-housing-page-4";
        }
        ServiceDto[] serviceDtoSToBeAdd = (ServiceDto[]) session.getAttribute("serviceDtoSToBeAdd");
        ServiceDto[] serviceDtoS = (ServiceDto[]) session.getAttribute("serviceDtoS");
        ArrayList<ServiceDto> tempServiceDtoSToBeAdd = new ArrayList<>(Arrays.asList(serviceDtoSToBeAdd));
        ArrayList<ServiceDto> tempServiceDtoS = new ArrayList<>(Arrays.asList(serviceDtoS));
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setName(name);
        tempServiceDtoSToBeAdd.add(serviceDto);
        tempServiceDtoS.add(serviceDto);
        serviceDtoSToBeAdd = tempServiceDtoSToBeAdd.toArray(new ServiceDto[0]);
        serviceDtoS = tempServiceDtoS.toArray(new ServiceDto[0]);
        session.setAttribute("serviceDtoSToBeAdd", serviceDtoSToBeAdd);
        session.setAttribute("serviceDtoS", serviceDtoS);
        model.addAttribute("serviceDtoS", serviceDtoS);
        return "add-housing-page-4";
    }

    @PostMapping(path = "/add-housing/page-4/delete")
    public String processDeleteAddHousingPage4(@RequestParam("name") String name, Model model, HttpSession session) {
        ServiceDto[] serviceDtoSToBeAdd = (ServiceDto[]) session.getAttribute("serviceDtoSToBeAdd");
        ServiceDto[] serviceDtoS = (ServiceDto[]) session.getAttribute("serviceDtoS");
        ArrayList<ServiceDto> tempServiceDtoSToBeAdd = new ArrayList<>(Arrays.asList(serviceDtoSToBeAdd));
        ArrayList<ServiceDto> tempServiceDtoS = new ArrayList<>(Arrays.asList(serviceDtoS));
        tempServiceDtoSToBeAdd.removeIf(tempServiceDtoToBeAdd -> Objects.equals(tempServiceDtoToBeAdd.getName(), name));
        tempServiceDtoS.removeIf(tempServiceDto -> Objects.equals(tempServiceDto.getName(), name));
        serviceDtoSToBeAdd = tempServiceDtoSToBeAdd.toArray(new ServiceDto[0]);
        serviceDtoS = tempServiceDtoS.toArray(new ServiceDto[0]);
        session.setAttribute("serviceDtoSToBeAdd", serviceDtoSToBeAdd);
        session.setAttribute("serviceDtoS", serviceDtoS);
        model.addAttribute("serviceDtoS", serviceDtoS);
        return "add-housing-page-4";
    }

    @PostMapping("/add-housing/page-4")
    public String processAddHousingPage4(Model model, HttpSession session) {
        ConstraintDto[] constraintDtoSToBeAdd = new ConstraintDto[0];
        ConstraintDto[] constraintDtoS = new ConstraintDto[0];
        session.setAttribute("constraintDtoSToBeAdd", constraintDtoSToBeAdd);
        session.setAttribute("constraintDtoS", constraintDtoS);
        model.addAttribute("constraintDtoS", constraintDtoS);
        return "add-housing-page-5";
    }

    @PostMapping("/add-housing/page-5/upload")
    public String processUploadAddHousingPage5(@RequestParam("name") String name, Model model, HttpSession session) {
        if (name.isEmpty()) {
            model.addAttribute("error", "Veuillez saisir un nom !");
            return "add-housing-page-5";
        }
        ConstraintDto[] constraintDtoSToBeAdd = (ConstraintDto[]) session.getAttribute("constraintDtoSToBeAdd");
        ConstraintDto[] constraintDtoS = (ConstraintDto[]) session.getAttribute("constraintDtoS");
        ArrayList<ConstraintDto> tempConstraintDtoSToBeAdd = new ArrayList<>(Arrays.asList(constraintDtoSToBeAdd));
        ArrayList<ConstraintDto> tempConstraintDtoS = new ArrayList<>(Arrays.asList(constraintDtoS));
        ConstraintDto constraintDto = new ConstraintDto();
        constraintDto.setName(name);
        tempConstraintDtoSToBeAdd.add(constraintDto);
        tempConstraintDtoS.add(constraintDto);
        constraintDtoSToBeAdd = tempConstraintDtoSToBeAdd.toArray(new ConstraintDto[0]);
        constraintDtoS = tempConstraintDtoS.toArray(new ConstraintDto[0]);
        session.setAttribute("constraintDtoSToBeAdd", constraintDtoSToBeAdd);
        session.setAttribute("constraintDtoS", constraintDtoS);
        model.addAttribute("constraintDtoS", constraintDtoS);
        return "add-housing-page-5";
    }

    @PostMapping(path = "/add-housing/page-5/delete")
    public String processDeleteAddHousingPage5(@RequestParam("name") String name, Model model, HttpSession session) {
        ConstraintDto[] constraintDtoSToBeAdd = (ConstraintDto[]) session.getAttribute("constraintDtoSToBeAdd");
        ConstraintDto[] constraintDtoS = (ConstraintDto[]) session.getAttribute("constraintDtoS");
        ArrayList<ConstraintDto> tempConstraintDtoSToBeAdd = new ArrayList<>(Arrays.asList(constraintDtoSToBeAdd));
        ArrayList<ConstraintDto> tempConstraintDtoS = new ArrayList<>(Arrays.asList(constraintDtoS));
        tempConstraintDtoSToBeAdd.removeIf(tempConstraintDtoToBeAdd -> Objects.equals(tempConstraintDtoToBeAdd.getName(), name));
        tempConstraintDtoS.removeIf(tempConstraintDto -> Objects.equals(tempConstraintDto.getName(), name));
        constraintDtoSToBeAdd = tempConstraintDtoSToBeAdd.toArray(new ConstraintDto[0]);
        constraintDtoS = tempConstraintDtoS.toArray(new ConstraintDto[0]);
        session.setAttribute("constraintDtoSToBeAdd", constraintDtoSToBeAdd);
        session.setAttribute("constraintDtoS", constraintDtoS);
        model.addAttribute("constraintDtoS", constraintDtoS);
        return "add-housing-page-5";
    }

    @PostMapping("/add-housing/page-5")
    public String processAddHousingPage5(HttpSession session, Authentication authentication) {
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        HousingDto housingDto = (HousingDto) session.getAttribute("housingDto");
        String uuid = (String) session.getAttribute("uuid");
        AddressEntity addressEntity = addressService.saveAddress(addressDto);
        UserEntity userEntity = userService.findUserByEmail(authentication.getName());
        HousingEntity housingEntity = housingService.saveHousing(housingDto, uuid, addressEntity, userEntity);
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        for (PhotoDto tempPhotoDto : tempPhotoDtoS) {
            photoService.savePhoto(tempPhotoDto, housingEntity);
        }
        ServiceDto[] serviceDtoSToBeAdd = (ServiceDto[]) session.getAttribute("serviceDtoSToBeAdd");
        ArrayList<ServiceDto> tempServiceDtoSToBeAdd = new ArrayList<>(Arrays.asList(serviceDtoSToBeAdd));
        for (ServiceDto tempServiceDtoToBeAdd : tempServiceDtoSToBeAdd) {
            serviceService.saveService(tempServiceDtoToBeAdd, housingEntity);
        }
        ConstraintDto[] constraintDtoSToBeAdd = (ConstraintDto[]) session.getAttribute("constraintDtoSToBeAdd");
        ArrayList<ConstraintDto> tempConstraintDtoSToBeAdd = new ArrayList<>(Arrays.asList(constraintDtoSToBeAdd));
        for (ConstraintDto tempConstraintDtoToBeAdd : tempConstraintDtoSToBeAdd) {
            constraintService.saveConstraint(tempConstraintDtoToBeAdd, housingEntity);
        }
        return "redirect:/profile";
    }

    @GetMapping("/edit-housing/{id}")
    public String editHousing(@PathVariable("id") Long id, Model model, HttpSession session) {
        HousingEntity housingEntity = housingService.findHousingById(id);
        AddressEntity addressEntity = housingEntity.getAddressEntity();
        AddressDto addressDto = new AddressDto();
        addressDto.setNumber(addressEntity.getNumber());
        addressDto.setStreet(addressEntity.getStreet());
        addressDto.setCity(addressEntity.getCity());
        addressDto.setCode(addressEntity.getCode());
        addressDto.setCountry(addressEntity.getCountry());
        session.setAttribute("housingEntity", housingEntity);
        session.setAttribute("addressEntity", addressEntity);
        model.addAttribute("addressEntity", addressEntity);
        model.addAttribute("addressDto", addressDto);
        return "edit-housing-page-1";
    }

    @PostMapping("/edit-housing/page-1")
    public String processEditHousingPage1(@Valid @ModelAttribute("addressDto") AddressDto addressDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("addressDto", addressDto);
            return "edit-housing-page-1";
        }
        HousingEntity housingEntity = (HousingEntity) session.getAttribute("housingEntity");
        HousingDto housingDto = new HousingDto();
        housingDto.setType(housingEntity.getType());
        housingDto.setName(housingEntity.getName());
        housingDto.setDescription(housingEntity.getDescription());
        session.setAttribute("addressDto", addressDto);
        session.setAttribute("housingEntity", housingEntity);
        model.addAttribute("housingEntity", housingEntity);
        model.addAttribute("housingDto", housingDto);
        return "edit-housing-page-2";
    }

    @PostMapping("/edit-housing/page-2")
    public String processEditHousingPage2(@Valid @ModelAttribute("housingDto") HousingDto housingDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("housingDto", housingDto);
            return "edit-housing-page-2";
        }
        HousingEntity housingEntity = (HousingEntity) session.getAttribute("housingEntity");
        Set<PhotoEntity> photoEntities = photoService.findPhotosByHousing(housingEntity);
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>();
        for (PhotoEntity photoEntity : photoEntities) {
            PhotoDto photoDto = new PhotoDto();
            photoDto.setId(photoEntity.getIdPhoto());
            photoDto.setName(photoEntity.getName());
            tempPhotoDtoS.add(photoDto);
        }
        String uuid = housingEntity.getUuid();
        String prefixFromLocal = "C:/wamp64/www/images/housings/housing-" + uuid + "/";
        String prefixFromServer = "http://localhost/images/housings/housing-" + uuid + "/";
        PhotoDto[] photoDtoSToBeAdd = new PhotoDto[0];
        PhotoDto[] photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        session.setAttribute("housingDto", housingDto);
        session.setAttribute("prefixFromLocal", prefixFromLocal);
        session.setAttribute("prefixFromServer", prefixFromServer);
        session.setAttribute("photoDtoSToBeAdd", photoDtoSToBeAdd);
        session.setAttribute("photoDtoS", photoDtoS);
        model.addAttribute("photoDtoS", photoDtoS);
        return "edit-housing-page-3";
    }

    @PostMapping("/edit-housing/page-3/upload")
    public String processUploadEditHousingPage3(@RequestParam("file") MultipartFile file, Model model, HttpSession session) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Veuillez choisir un fichier !");
            return "edit-housing-page-3";
        }
        String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
        try {
            FileUpload.saveFile(prefixFromLocal, file);
        } catch (IOException e) {
            model.addAttribute("error", "Il y a eu une erreur !");
            return "edit-housing-page-3";
        }
        PhotoDto[] photoDtoSToBeAdd = (PhotoDto[]) session.getAttribute("photoDtoSToBeAdd");
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> tempPhotoDtoSToBeAdd = new ArrayList<>(Arrays.asList(photoDtoSToBeAdd));
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        String prefixFromServer = (String) session.getAttribute("prefixFromServer");
        String fileName = file.getOriginalFilename();
        PhotoDto photoDto = new PhotoDto();
        photoDto.setName(fileName);
        photoDto.setPath(prefixFromServer + fileName);
        tempPhotoDtoSToBeAdd.add(photoDto);
        tempPhotoDtoS.add(photoDto);
        photoDtoSToBeAdd = tempPhotoDtoSToBeAdd.toArray(new PhotoDto[0]);
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        session.setAttribute("photoDtoSToBeAdd", photoDtoSToBeAdd);
        session.setAttribute("photoDtoS", photoDtoS);
        model.addAttribute("photoDtoS", photoDtoS);
        return "edit-housing-page-3";
    }

    @PostMapping(path = "/edit-housing/page-3/delete")
    public String processDeleteEditHousingPage3(@RequestParam("name") String name, Model model, HttpSession session) {
        PhotoDto[] photoDtoSToBeAdd = (PhotoDto[]) session.getAttribute("photoDtoSToBeAdd");
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> tempPhotoDtoSToBeAdd = new ArrayList<>(Arrays.asList(photoDtoSToBeAdd));
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        tempPhotoDtoSToBeAdd.removeIf(tempPhotoDtoToBeAdd -> Objects.equals(tempPhotoDtoToBeAdd.getName(), name));
        int i = 0;
        while (i < tempPhotoDtoS.size()) {
            PhotoDto tempPhotoDto = tempPhotoDtoS.get(i);
            String tempPhotoDtoName = tempPhotoDto.getName();
            if (Objects.equals(tempPhotoDtoName, name)) {
                tempPhotoDtoS.remove(i);
                String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
                photoService.deletePhotoById(tempPhotoDto.getId());
                try {
                    FileUpload.deleteFile(prefixFromLocal + tempPhotoDtoName);
                } catch (IOException e) {
                    model.addAttribute("error", "Il y a eu une erreur !");
                    return "edit-housing-page-3";
                }
            } else {
                i++;
            }
        }
        photoDtoSToBeAdd = tempPhotoDtoSToBeAdd.toArray(new PhotoDto[0]);
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        session.setAttribute("photoDtoSToBeAdd", photoDtoSToBeAdd);
        session.setAttribute("photoDtoS", photoDtoS);
        model.addAttribute("photoDtoS", photoDtoS);
        return "edit-housing-page-3";
    }

    @PostMapping("/edit-housing/page-3")
    public String processEditHousingPage3(Model model, HttpSession session) {
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        if (tempPhotoDtoS.isEmpty()) {
            model.addAttribute("error", "Veuillez ajouter au moins une photo !");
            return "edit-housing-page-3";
        }
        HousingEntity housingEntity = (HousingEntity) session.getAttribute("housingEntity");
        Set<ServiceEntity> serviceEntities = serviceService.findServicesByHousing(housingEntity);
        ArrayList<ServiceDto> tempServiceDtoS = new ArrayList<>();
        for (ServiceEntity serviceEntity : serviceEntities) {
            ServiceDto serviceDto = new ServiceDto();
            serviceDto.setId(serviceEntity.getIdService());
            serviceDto.setName(serviceEntity.getName());
            tempServiceDtoS.add(serviceDto);
        }
        ServiceDto[] serviceDtoSToBeAdd = new ServiceDto[0];
        ServiceDto[] serviceDtoS = tempServiceDtoS.toArray(new ServiceDto[0]);
        session.setAttribute("serviceDtoSToBeAdd", serviceDtoSToBeAdd);
        session.setAttribute("serviceDtoS", serviceDtoS);
        model.addAttribute("serviceDtoS", serviceDtoS);
        return "edit-housing-page-4";
    }

    @PostMapping("/edit-housing/page-4/upload")
    public String processUploadEditHousingPage4(@RequestParam("name") String name, Model model, HttpSession session) {
        if (name.isEmpty()) {
            model.addAttribute("error", "Veuillez saisir un nom !");
            return "edit-housing-page-4";
        }
        ServiceDto[] serviceDtoSToBeAdd = (ServiceDto[]) session.getAttribute("serviceDtoSToBeAdd");
        ServiceDto[] serviceDtoS = (ServiceDto[]) session.getAttribute("serviceDtoS");
        ArrayList<ServiceDto> tempServiceDtoSToBeAdd = new ArrayList<>(Arrays.asList(serviceDtoSToBeAdd));
        ArrayList<ServiceDto> tempServiceDtoS = new ArrayList<>(Arrays.asList(serviceDtoS));
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setName(name);
        tempServiceDtoSToBeAdd.add(serviceDto);
        tempServiceDtoS.add(serviceDto);
        serviceDtoSToBeAdd = tempServiceDtoSToBeAdd.toArray(new ServiceDto[0]);
        serviceDtoS = tempServiceDtoS.toArray(new ServiceDto[0]);
        session.setAttribute("serviceDtoSToBeAdd", serviceDtoSToBeAdd);
        session.setAttribute("serviceDtoS", serviceDtoS);
        model.addAttribute("serviceDtoS", serviceDtoS);
        return "edit-housing-page-4";
    }

    @PostMapping(path = "/edit-housing/page-4/delete")
    public String processDeleteEditHousingPage4(@RequestParam("name") String name, Model model, HttpSession session) {
        ServiceDto[] serviceDtoSToBeAdd = (ServiceDto[]) session.getAttribute("serviceDtoSToBeAdd");
        ServiceDto[] serviceDtoS = (ServiceDto[]) session.getAttribute("serviceDtoS");
        ArrayList<ServiceDto> tempServiceDtoSToBeAdd = new ArrayList<>(Arrays.asList(serviceDtoSToBeAdd));
        ArrayList<ServiceDto> tempServiceDtoS = new ArrayList<>(Arrays.asList(serviceDtoS));
        int i = 0;
        while (i < tempServiceDtoS.size()) {
            ServiceDto tempServiceDto = tempServiceDtoS.get(i);
            if (Objects.equals(tempServiceDto.getName(), name)) {
                tempServiceDtoS.remove(i);
                serviceService.deleteServiceById(tempServiceDto.getId());
            } else {
                i++;
            }
        }
        serviceDtoSToBeAdd = tempServiceDtoSToBeAdd.toArray(new ServiceDto[0]);
        serviceDtoS = tempServiceDtoS.toArray(new ServiceDto[0]);
        session.setAttribute("serviceDtoSToBeAdd", serviceDtoSToBeAdd);
        session.setAttribute("serviceDtoS", serviceDtoS);
        model.addAttribute("serviceDtoS", serviceDtoS);
        return "edit-housing-page-4";
    }

    @PostMapping("/edit-housing/page-4")
    public String processEditHousingPage4(Model model, HttpSession session) {
        HousingEntity housingEntity = (HousingEntity) session.getAttribute("housingEntity");
        Set<ConstraintEntity> constraintEntities = constraintService.findConstraintsByHousing(housingEntity);
        ArrayList<ConstraintDto> tempConstraintDtoS = new ArrayList<>();
        for (ConstraintEntity constraintEntity : constraintEntities) {
            ConstraintDto constraintDto = new ConstraintDto();
            constraintDto.setId(constraintEntity.getIdConstraint());
            constraintDto.setName(constraintEntity.getName());
            tempConstraintDtoS.add(constraintDto);
        }
        ConstraintDto[] constraintDtoSToBeAdd = new ConstraintDto[0];
        ConstraintDto[] constraintDtoS = tempConstraintDtoS.toArray(new ConstraintDto[0]);
        session.setAttribute("constraintDtoSToBeAdd", constraintDtoSToBeAdd);
        session.setAttribute("constraintDtoS", constraintDtoS);
        model.addAttribute("constraintDtoS", constraintDtoS);
        return "edit-housing-page-5";
    }

    @PostMapping("/edit-housing/page-5/upload")
    public String processUploadEditHousingPage5(@RequestParam("name") String name, Model model, HttpSession session) {
        if (name.isEmpty()) {
            model.addAttribute("error", "Veuillez saisir un nom !");
            return "edit-housing-page-5";
        }
        ConstraintDto[] constraintDtoSTobeAdd = (ConstraintDto[]) session.getAttribute("constraintDtoSToBeAdd");
        ConstraintDto[] constraintDtoS = (ConstraintDto[]) session.getAttribute("constraintDtoS");
        ArrayList<ConstraintDto> tempConstraintDtoSToBeAdd = new ArrayList<>(Arrays.asList(constraintDtoSTobeAdd));
        ArrayList<ConstraintDto> tempConstraintDtoS = new ArrayList<>(Arrays.asList(constraintDtoS));
        ConstraintDto constraintDto = new ConstraintDto();
        constraintDto.setName(name);
        tempConstraintDtoSToBeAdd.add(constraintDto);
        tempConstraintDtoS.add(constraintDto);
        constraintDtoSTobeAdd = tempConstraintDtoSToBeAdd.toArray(new ConstraintDto[0]);
        constraintDtoS = tempConstraintDtoS.toArray(new ConstraintDto[0]);
        session.setAttribute("constraintDtoSToBeAdd", constraintDtoSTobeAdd);
        session.setAttribute("constraintDtoS", constraintDtoS);
        model.addAttribute("constraintDtoS", constraintDtoS);
        return "edit-housing-page-5";
    }

    @PostMapping(path = "/edit-housing/page-5/delete")
    public String processDeleteEditHousingPage5(@RequestParam("name") String name, Model model, HttpSession session) {
        ConstraintDto[] constraintDtoSTobeAdd = (ConstraintDto[]) session.getAttribute("constraintDtoSToBeAdd");
        ConstraintDto[] constraintDtoS = (ConstraintDto[]) session.getAttribute("constraintDtoS");
        ArrayList<ConstraintDto> tempConstraintDtoSToBeAdd = new ArrayList<>(Arrays.asList(constraintDtoSTobeAdd));
        ArrayList<ConstraintDto> tempConstraintDtoS = new ArrayList<>(Arrays.asList(constraintDtoS));
        int i = 0;
        while (i < tempConstraintDtoS.size()) {
            ConstraintDto tempConstraintDto = tempConstraintDtoS.get(i);
            if (Objects.equals(tempConstraintDto.getName(), name)) {
                tempConstraintDtoS.remove(i);
                serviceService.deleteServiceById(tempConstraintDto.getId());
            } else {
                i++;
            }
        }
        constraintDtoSTobeAdd = tempConstraintDtoSToBeAdd.toArray(new ConstraintDto[0]);
        constraintDtoS = tempConstraintDtoS.toArray(new ConstraintDto[0]);
        session.setAttribute("constraintDtoSToBeAdd", constraintDtoSTobeAdd);
        session.setAttribute("constraintDtoS", constraintDtoS);
        model.addAttribute("constraintDtoS", constraintDtoS);
        return "edit-housing-page-5";
    }

    @PostMapping("/edit-housing/page-5")
    public String processEditHousingPage5(HttpSession session) {
        AddressEntity addressEntity = (AddressEntity) session.getAttribute("addressEntity");
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        HousingEntity housingEntity = (HousingEntity) session.getAttribute("housingEntity");
        HousingDto housingDto = (HousingDto) session.getAttribute("housingDto");
        addressService.updateAddress(addressEntity, addressDto);
        housingService.updateHousing(housingEntity, housingDto, addressEntity);
        PhotoDto[] photoDtoSToBeAdd = (PhotoDto[]) session.getAttribute("photoDtoSToBeAdd");
        ArrayList<PhotoDto> tempPhotoDtoSToBeAdd = new ArrayList<>(Arrays.asList(photoDtoSToBeAdd));
        for (PhotoDto tempPhotoDtoToBeAdd : tempPhotoDtoSToBeAdd) {
            photoService.savePhoto(tempPhotoDtoToBeAdd, housingEntity);
        }
        ServiceDto[] serviceDtoSToBeAdd = (ServiceDto[]) session.getAttribute("serviceDtoSToBeAdd");
        ArrayList<ServiceDto> tempServiceDtoSToBeAdd = new ArrayList<>(Arrays.asList(serviceDtoSToBeAdd));
        for (ServiceDto tempServiceDtoToBeAdd : tempServiceDtoSToBeAdd) {
            serviceService.saveService(tempServiceDtoToBeAdd, housingEntity);
        }
        ConstraintDto[] constraintDtoSToBeAdd = (ConstraintDto[]) session.getAttribute("constraintDtoSToBeAdd");
        ArrayList<ConstraintDto> tempConstraintDtoSToBeAdd = new ArrayList<>(Arrays.asList(constraintDtoSToBeAdd));
        for (ConstraintDto tempConstraintDtoToBeAdd : tempConstraintDtoSToBeAdd) {
            constraintService.saveConstraint(tempConstraintDtoToBeAdd, housingEntity);
        }
        return "redirect:/profile";
    }

    @GetMapping("/delete-housing/{id}")
    public String deleteHousing(@PathVariable("id") Long id) {
        AddressEntity addressEntity = housingService.deleteHousingById(id);
        addressService.deleteAddressById(addressEntity.getIdAddress());
        return "redirect:/profile";
    }

    @GetMapping("/view-housing/{id}")
    public String viewHousing(@PathVariable("id") Long id, Model model) {
        HousingEntity housingEntity = housingService.findHousingById(id);
        Set<PhotoEntity> photoEntities = photoService.findPhotosByHousing(housingEntity);
        Set<ServiceEntity> serviceEntities = serviceService.findServicesByHousing(housingEntity);
        Set<ConstraintEntity> constraintEntities = constraintService.findConstraintsByHousing(housingEntity);
        model.addAttribute("housingEntity", housingEntity);
        model.addAttribute("photoEntities", photoEntities);
        model.addAttribute("serviceEntities", serviceEntities);
        model.addAttribute("constraintEntities", constraintEntities);
        return "view-housing";
    }

    @GetMapping("/book-housing/{id}")
    public String bookHousing(@PathVariable("id") Long id, Model model) {
        BookingDto bookingDto = new BookingDto();
        model.addAttribute("bookingDto", bookingDto);
        return "book-housing";
    }

    @PostMapping("/book-housing")
    public String processBooking(@Valid @ModelAttribute("bookingDto") BookingDto bookingDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bookingDto", bookingDto);
            return "book-housing";
        }
        return "redirect:/book-housing?success";
    }

}