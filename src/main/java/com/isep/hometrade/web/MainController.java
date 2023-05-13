package com.isep.hometrade.web;

import com.isep.hometrade.business.AddressEntity;
import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.business.PhotoEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.map.AddressDto;
import com.isep.hometrade.map.PhotoDto;
import com.isep.hometrade.service.AddressService;
import com.isep.hometrade.service.HouseService;
import com.isep.hometrade.service.PhotoService;
import com.isep.hometrade.service.UserService;
import com.isep.hometrade.map.HouseDto;
import com.isep.hometrade.map.UserDto;
import com.isep.hometrade.util.FileUpload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final HouseService houseService;
    private final PhotoService photoService;


    @Autowired
    public MainController(UserService userService, AddressService addressService, HouseService houseService, PhotoService photoService) {
        this.userService = userService;
        this.addressService = addressService;
        this.houseService = houseService;
        this.photoService = photoService;
    }

    @GetMapping("/")
    public String welcome(Model model) {
        Set<HouseEntity> houseEntities = houseService.find5LatestHouses();
        model.addAttribute("houseEntities", houseEntities);
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
        Set<HouseEntity> houseEntities = houseService.findHousesByUser(userEntity);
        model.addAttribute("userEntity", userEntity);
        model.addAttribute("houseEntities", houseEntities);
        return "profile";
    }

    @GetMapping("/add-house")
    public String addHouse(Model model) {
        AddressDto addressDto = new AddressDto();
        model.addAttribute("addressDto", addressDto);
        return "add-house-page-1";
    }

    @PostMapping("/add-house/page-1")
    public String processAddHousePage1(@Valid @ModelAttribute("addressDto") AddressDto addressDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("addressDto", addressDto);
            return "add-house-page-1";
        }
        HouseDto houseDto = new HouseDto();
        session.setAttribute("addressDto", addressDto);
        model.addAttribute("houseDto", houseDto);
        return "add-house-page-2";
    }

    @PostMapping("/add-house/page-2")
    public String processAddHousePage2(@Valid @ModelAttribute("houseDto") HouseDto houseDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("houseDto", houseDto);
            return "add-house-page-2";
        }
        String uuid = UUID.randomUUID().toString();
        String prefixFromLocal = "C:/wamp64/www/images/houses/house-" + uuid + "/";
        String prefixFromServer = "http://localhost/images/houses/house-" + uuid + "/";
        PhotoDto[] photoDtoS = new PhotoDto[0];
        String[] names = new String[0];
        session.setAttribute("houseDto", houseDto);
        session.setAttribute("uuid", uuid);
        session.setAttribute("prefixFromLocal", prefixFromLocal);
        session.setAttribute("prefixFromServer", prefixFromServer);
        session.setAttribute("photoDtoS", photoDtoS);
        session.setAttribute("names", names);
        model.addAttribute("names", names);
        return "add-house-page-3";
    }

    @PostMapping("/add-house/page-3/upload")
    public String processUploadAddHousePage3(@RequestParam("file") MultipartFile file, Model model, HttpSession session) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Veuillez sélectionner un fichier !");
            return "add-house-page-3";
        }
        String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
        try {
            FileUpload.saveFile(prefixFromLocal, file);
        } catch (IOException e) {
            model.addAttribute("error", "Il y a eu une erreur !");
            return "add-house-page-3";
        }
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        String[] names = (String[]) session.getAttribute("names");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        ArrayList<String> tempNames = new ArrayList<>(Arrays.asList(names));
        String prefixFromServer = (String) session.getAttribute("prefixFromServer");
        String fileName = file.getOriginalFilename();
        PhotoDto photoDto = new PhotoDto(fileName, prefixFromServer + fileName);
        tempPhotoDtoS.add(photoDto);
        tempNames.add(fileName);
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        names = tempNames.toArray(new String[0]);
        session.setAttribute("photoDtoS", photoDtoS);
        session.setAttribute("names", names);
        model.addAttribute("names", names);
        return "add-house-page-3";
    }

    @PostMapping(path = "/add-house/page3/delete")
    public String processDeleteAddHousePage3(@RequestParam("name") String name, Model model, HttpSession session) {
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        String[] names = (String[]) session.getAttribute("names");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        ArrayList<String> tempNames = new ArrayList<>(Arrays.asList(names));
        int i = 0;
        while (i < tempPhotoDtoS.size()) {
            String tempPhotoName = tempPhotoDtoS.get(i).getName();
            if (Objects.equals(tempPhotoName, name)) {
                tempPhotoDtoS.remove(i);
                String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
                try {
                    FileUpload.deleteFile(prefixFromLocal + tempPhotoName);
                } catch (IOException e) {
                    model.addAttribute("error", "Il y a eu une erreur !");
                    return "add-house-page-3";
                }
            } else {
                i++;
            }
        }
        tempNames.removeIf(tempName -> Objects.equals(tempName, name));
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        names = tempNames.toArray(new String[0]);
        session.setAttribute("photoDtoS", photoDtoS);
        session.setAttribute("names", names);
        model.addAttribute("names", names);
        return "add-house-page-3";
    }

    @PostMapping("/add-house/page3")
    public String processAddHousePage3(Model model, HttpSession session,  Authentication authentication) {
        String[] names = (String[]) session.getAttribute("names");
        ArrayList<String> tempNames = new ArrayList<>(Arrays.asList(names));
        if (tempNames.isEmpty()) {
            model.addAttribute("error", "Veuillez ajouter au moins une photo !");
            return "add-house-page-3";
        }
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        HouseDto houseDto = (HouseDto) session.getAttribute("houseDto");
        String uuid = (String) session.getAttribute("uuid");
        AddressEntity addressEntity = addressService.saveAddress(addressDto);
        UserEntity userEntity = userService.findUserByEmail(authentication.getName());
        HouseEntity houseEntity = houseService.saveHouse(houseDto, uuid, addressEntity, userEntity);
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        for (PhotoDto tempPhotoDto :tempPhotoDtoS) {
            photoService.savePhoto(tempPhotoDto,houseEntity);
        }
        return "redirect:/profile";
    }

    @GetMapping("/edit-house/{id}")
    public String editHouse(@PathVariable("id") Long id, Model model, HttpSession session) {
        HouseEntity houseEntity = houseService.findHouseById(id);
        AddressEntity addressEntity = houseEntity.getAddressEntity();
        AddressDto addressDto = new AddressDto();
        addressDto.setNumber(addressEntity.getNumber());
        addressDto.setStreet(addressEntity.getStreet());
        addressDto.setCity(addressEntity.getCity());
        addressDto.setCode(addressEntity.getCode());
        addressDto.setCountry(addressEntity.getCountry());
        session.setAttribute("houseEntity", houseEntity);
        session.setAttribute("addressEntity", addressEntity);
        model.addAttribute("addressEntity", addressEntity);
        model.addAttribute("addressDto", addressDto);
        return "edit-house-page-1";
    }

    @PostMapping("/edit-house/page-1")
    public String processEditHousePage1(@Valid @ModelAttribute("addressDto") AddressDto addressDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("addressDto", addressDto);
            return "edit-house-page-1";
        }
        HouseEntity houseEntity = (HouseEntity) session.getAttribute("houseEntity");
        HouseDto houseDto = new HouseDto();
        houseDto.setName(houseEntity.getName());
        houseDto.setDescription(houseEntity.getDescription());
        session.setAttribute("addressDto", addressDto);
        session.setAttribute("houseEntity", houseEntity);
        model.addAttribute("houseEntity", houseEntity);
        model.addAttribute("houseDto", houseDto);
        return "edit-house-page-2";
    }

    @PostMapping("/edit-house/page-2")
    public String processEditHousePage2(@Valid @ModelAttribute("houseDto") HouseDto houseDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("houseDto", houseDto);
            return "edit-house-page-2";
        }
        HouseEntity houseEntity = (HouseEntity) session.getAttribute("houseEntity");
        Set<PhotoEntity> photoEntities = photoService.findPhotosByHouse(houseEntity);
        ArrayList<String> tempNames = new ArrayList<>();
        for (PhotoEntity photoEntity : photoEntities) {
            tempNames.add(photoEntity.getName());
        }
        String uuid = houseEntity.getUuid();
        String prefixFromLocal = "C:/wamp64/www/images/houses/house-" + uuid + "/";
        String prefixFromServer = "http://localhost/images/houses/house-" + uuid + "/";
        PhotoDto[] photoDtoS = new PhotoDto[0];
        String[] names = tempNames.toArray(new String[0]);
        session.setAttribute("houseDto", houseDto);
        session.setAttribute("prefixFromLocal", prefixFromLocal);
        session.setAttribute("prefixFromServer", prefixFromServer);
        session.setAttribute("photoDtoS", photoDtoS);
        session.setAttribute("names", names);
        model.addAttribute("names", names);
        return "edit-house-page-3";
    }

    @PostMapping("/edit-house/page-3/upload")
    public String processUploadEditHousePage3(@RequestParam("file") MultipartFile file, Model model, HttpSession session) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Veuillez sélectionner un fichier !");
            return "edit-house-page-3";
        }
        String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
        try {
            FileUpload.saveFile(prefixFromLocal, file);
        } catch (IOException e) {
            model.addAttribute("error", "Il y a eu une erreur !");
            return "edit-house-page-3";
        }
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        String[] names = (String[]) session.getAttribute("names");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        ArrayList<String> tempNames = new ArrayList<>(Arrays.asList(names));
        String prefixFromServer = (String) session.getAttribute("prefixFromServer");
        String fileName = file.getOriginalFilename();
        PhotoDto photoDto = new PhotoDto(fileName, prefixFromServer + fileName);
        tempPhotoDtoS.add(photoDto);
        tempNames.add(fileName);
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        names = tempNames.toArray(new String[0]);
        session.setAttribute("photoDtoS", photoDtoS);
        session.setAttribute("names", names);
        model.addAttribute("names", names);
        return "edit-house-page-3";
    }

    @PostMapping(path = "/edit-house/page3/delete")
    public String processDeleteEditHousePage3(@RequestParam("name") String name, Model model, HttpSession session) {
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        String[] names = (String[]) session.getAttribute("names");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        ArrayList<String> tempNames = new ArrayList<>(Arrays.asList(names));
        int i = 0;
        while (i < tempPhotoDtoS.size()) {
            String tempPhotoName = tempPhotoDtoS.get(i).getName();
            if (Objects.equals(tempPhotoName, name)) {
                tempPhotoDtoS.remove(i);
            } else {
                i++;
            }
        }
        i = 0;
        while (i < tempNames.size()) {
            String tempName = tempNames.get(i);
            if (Objects.equals(tempName, name)) {
                tempNames.remove(i);
                String prefixFromLocal = (String) session.getAttribute("prefixFromLocal");
                String prefixFromServer = (String) session.getAttribute("prefixFromServer");
                photoService.deletePhotoByPath(prefixFromServer + tempName);
                try {
                    FileUpload.deleteFile(prefixFromLocal + tempName);
                } catch (IOException e) {
                    model.addAttribute("error", "Il y a eu une erreur !");
                    return "edit-house-page-3";
                }
            } else {
                i++;
            }
        }
        photoDtoS = tempPhotoDtoS.toArray(new PhotoDto[0]);
        names = tempNames.toArray(new String[0]);
        session.setAttribute("photoDtoS", photoDtoS);
        session.setAttribute("names", names);
        model.addAttribute("names", names);
        return "edit-house-page-3";
    }

    @PostMapping("/edit-house/page3")
    public String processEditHousePage3(Model model, HttpSession session) {
        String[] names = (String[]) session.getAttribute("names");
        ArrayList<String> tempNames = new ArrayList<>(Arrays.asList(names));
        if (tempNames.isEmpty()) {
            model.addAttribute("error", "Veuillez ajouter au moins une photo !");
            return "edit-house-page-3";
        }
        AddressEntity addressEntity = (AddressEntity) session.getAttribute("addressEntity");
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        HouseEntity houseEntity = (HouseEntity) session.getAttribute("houseEntity");
        HouseDto houseDto = (HouseDto) session.getAttribute("houseDto");
        addressService.updateAddress(addressEntity, addressDto);
        houseService.updateHouse(houseEntity, houseDto, addressEntity);
        PhotoDto[] photoDtoS = (PhotoDto[]) session.getAttribute("photoDtoS");
        ArrayList<PhotoDto> tempPhotoDtoS = new ArrayList<>(Arrays.asList(photoDtoS));
        for (PhotoDto tempPhotoDto :tempPhotoDtoS) {
            photoService.savePhoto(tempPhotoDto,houseEntity);
        }
        return "redirect:/profile";
    }

    @GetMapping("/delete-house/{id}")
    public String deleteHouse(@PathVariable("id") Long id) {
        AddressEntity addressEntity = houseService.deleteHouseById(id);
        addressService.deleteAddressById(addressEntity.getIdAddress());
        return "redirect:/profile";
    }

}