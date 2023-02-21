package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.repositories.UserRepository;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.entities.FaqCategory;
 import tn.esprit.springfever.enums.Faq_Category_enum;
 import tn.esprit.springfever.repositories.FaqCategoryRepository;
import tn.esprit.springfever.repositories.FaqRepository;
import org.apache.poi.ss.usermodel.Row;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.*;

@Service
@Slf4j
public class ServiceFaqsImpl  implements IServiceFaq {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FaqRepository faqRepository;
    @Autowired
    FaqCategoryRepository faqCategoryRepository;


    @Override
    public Faq addFaq(Faq faq) {
        log.info("Faq was successfully added !");
        return faqRepository.save(faq);
    }

    @Override
    public List<Faq> getAllFaqs() {
        log.info("list of FAQs  : !");
        return faqRepository.findAll();
    }

    @Override
    public String deleteFaq(Long idFaq) {
        Faq ExistingFaq = faqRepository.findById(idFaq).orElse(null);
        if (ExistingFaq != null) {
            faqRepository.delete(ExistingFaq);
            log.info("Faq was successfully deleted !");
            return "Faq was successfully deleted !";
        }
        return "This Faq is not existing ! ";
    }

    @Override
    public Faq updateFaq(Long idFaq, Faq faq) {
        Faq existingFaq = faqRepository.findById(idFaq).orElse(null);
        if (existingFaq != null) {
            existingFaq.setQuestion(faq.getQuestion());
            existingFaq.setResponse(faq.getResponse());
            faqRepository.save(existingFaq);
            log.info("Faq was successfully updated !");
        }
        log.info("This Faq is not existing !");

        return existingFaq;
    }

    @Override
    public List<Faq> getAllFaqsByCategory(Faq_Category_enum faq_category_enum) {
        log.info("faqs list by category :!" + faq_category_enum.toString());
        return faqRepository.getAllByFaqCategoriesFaqCategory(faq_category_enum);
    }

    @Override
    public String AssignCategoryToFaq(Long idFaq, Long idFaqCategory) {
        Faq existingFaq = faqRepository.findById(idFaq).orElse(null);
        FaqCategory ExistingFaqCategory = faqCategoryRepository.findById(idFaqCategory).orElse(null);

        if ((existingFaq != null) && (ExistingFaqCategory != null)) {
            existingFaq.getFaqCategories().add(ExistingFaqCategory);
            faqRepository.save(existingFaq);
            log.info("FaqCategory  was successfully assigned to Faq !");
            return "FaqCategory  was successfully assigned to Faq !!";
        }
        return "Faq or Faq Category ot found ! ";
    }

    @Override
    public List<Faq> importFAQsFromExcel(MultipartFile file) throws IOException {


        List<Faq> faqs = new ArrayList<>();

        // Load the Excel file using Apache POI
        Workbook workbook = new  XSSFWorkbook(file.getInputStream()) {
         };

        // Get the first sheet of the workbook
        Sheet sheet = workbook.getSheetAt(0);

        // Loop through the rows of the sheet
        for (Row row : sheet) {
            Faq faq = new Faq();
            int i=1 ;
            // Set the properties of the FAQ object based on the cell values in the row
            faq.setQuestion(row.getCell(0).getStringCellValue());
            faq.setResponse(row.getCell(1).getStringCellValue());
            while((row.getLastCellNum()-1)>i) {
                faq.getFaqCategories().add(faqCategoryRepository.findByFaqCategory(Faq_Category_enum.valueOf(row.getCell(i+1).getStringCellValue())));
                i++ ;
            }
             faqs.add(faq);
         }
        // Save the list of FAQs to the database
        faqRepository.saveAll(faqs);

        return faqs ;

    }



}



