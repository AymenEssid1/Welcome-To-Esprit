package tn.esprit.springfever.Services.Implementation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import tn.esprit.springfever.Repositories.BadgeRepo;
import tn.esprit.springfever.Repositories.ImageRepository;
import tn.esprit.springfever.Repositories.RoleRepo;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.Services.Interface.IServiceUser;
import tn.esprit.springfever.entities.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
@Slf4j
public class UserIMP implements IServiceUser {

    @Autowired
    UserRepo userrepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    BadgeRepo badgeRepo;

    @Override
    public User addUserAndAssignRole(User user, RoleType rolename) {
        Role role = roleRepo.findByRolename(rolename);


        user.getRoles().add(role);
        return userrepo.save(user);
    }

    @Override
    public Badge addBadge(Badge badge, long userid) {
        User user = userrepo.findById(userid).orElse(null);
        badge.setUser(user);
        return badgeRepo.save(badge);
    }


    @Override
    public User addUser(User user) {
        return userrepo.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User p = userrepo.findById(Long.valueOf(id)).orElse(null);
        if (p != null) {
            user.setUserid(p.getUserid());
            ;
            userrepo.save(user);
        }
        return p;
    }

    @Override
    public String deleteUser(Long user) {
        User p = userrepo.findById(Long.valueOf(user)).orElse(null);
        if (p != null) {
            userrepo.delete(p);
            return "User was successfully deleted !";
        }
        return "Not Found ! ";

    }

    @Override
    public List<User> getAllUsers() {
        return userrepo.findAll();
    }

    @Override
    public User getSingleUser(Long id) {
        return userrepo.findById(id).orElse(null);
    }

    @Override
    public String generateQr(User user) {
        String qrCodeData = user.toString();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);
        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = new BitMatrix(8, 8);
        try {
            bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (WriterException e) {
            // Handle writer exception
            e.printStackTrace();

        }// Save QR code as a PNG file
        String filePath = System.getProperty("user.dir") + "/assets/" + "user-" + user.getUserid() + "-" + user.getCin() + ".png"; // Specify the file path and name
        File qrCodeFile = new File(filePath);
        try {
            MatrixToImageWriter.writeToFile(bitMatrix, "png", qrCodeFile);
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();

        }

        // Return a view that shows the generated QR code
        ModelAndView modelAndView = new ModelAndView("qr-code");
        modelAndView.addObject("qrCodeFile", qrCodeFile);
        return filePath;
    }


    @Transactional
    public void saveAll(List<User> users) {
        userrepo.saveAll(users);
    }

    public List<User> readUsersFromExcelFile(InputStream is) throws IOException {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0); // assuming the user data is in the first sheet
        List<User> users = new ArrayList<>();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // skip the header row
                continue;
            }

            User user = new User();
            user.setUsername(row.getCell(0).getStringCellValue());
            user.setEmail(row.getCell(1).getStringCellValue());
            user.setFirstname(row.getCell(2).getStringCellValue());
            user.setLastname(row.getCell(3).getStringCellValue());
            user.setCin((int) row.getCell(4).getNumericCellValue());
            user.setDob(row.getCell(5).getDateCellValue());
            user.setPassword(passwordEncoder.encode(row.getCell(6).getStringCellValue()));

            Role role = roleRepo.findByRolename(RoleType.CANDIDATE);

            if (row.getCell(7) == null) {
                user.getRoles().add(role);

            } else {
                List<String> roles = Arrays.asList(row.getCell(7).getStringCellValue().split("\\s*,\\s*"));
                roles.forEach(rolee -> {
                    for (RoleType r : RoleType.values()) {

                        if (r.name().equals(rolee)) {
                            Role rol = roleRepo.findByRolename(r);
                            user.getRoles().add(rol);
                            break;
                        }
                    }
                });
            }

            users.add(user);
        }

        workbook.close();
        return users;
    }

}
