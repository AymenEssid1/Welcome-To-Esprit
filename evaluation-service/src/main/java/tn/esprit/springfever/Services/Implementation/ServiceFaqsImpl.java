package tn.esprit.springfever.Services.Implementation;



  import io.github.flashvayne.chatgpt.service.ChatgptService;
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

  import java.io.BufferedReader;
  import java.io.FileReader;
  import java.io.IOException;
import java.util.ArrayList;
  import java.util.Collection;
  import java.util.List;
  import java.util.Map;
  import java.util.function.Function;
  import java.util.stream.Collectors;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
@Slf4j
 public class ServiceFaqsImpl  implements IServiceFaq {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FaqRepository faqRepository;
    @Autowired
    FaqCategoryRepository faqCategoryRepository;
    @Autowired
    ChatgptService chatgptService ;


    @Override
    public Faq addFaq(Faq faq) {
        LOGGER.info("Faq was successfully added !");
        return faqRepository.save(faq);
    }

    @Override
    public List<Faq> getAllFaqs() {
        LOGGER.info("list of FAQs  : !");
        return faqRepository.findAll();
    }

    @Override
    public String deleteFaq(Long idFaq) {
        Faq ExistingFaq = faqRepository.findById(idFaq).orElse(null);
        if (ExistingFaq != null) {
            faqRepository.delete(ExistingFaq);
            LOGGER.info("Faq was successfully deleted !");
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
            LOGGER.info("Faq was successfully updated !");
        }
        LOGGER.info("This Faq is not existing !");

        return existingFaq;
    }

    @Override
    public List<Faq> getAllFaqsByCategory(Faq_Category_enum faq_category_enum) {
        LOGGER.info("faqs list by category :!" + faq_category_enum.toString());
        return faqRepository.getAllByFaqCategoriesFaqCategory(faq_category_enum);
    }

    @Override
    public String AssignCategoryToFaq(Long idFaq, Long idFaqCategory) {
        Faq existingFaq = faqRepository.findById(idFaq).orElse(null);
        FaqCategory ExistingFaqCategory = faqCategoryRepository.findById(idFaqCategory).orElse(null);

        if ((existingFaq != null) && (ExistingFaqCategory != null)) {
            existingFaq.getFaqCategories().add(ExistingFaqCategory);
            faqRepository.save(existingFaq);
            LOGGER.info("FaqCategory  was successfully assigned to Faq !");
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

    @Override
    public List<Faq> search(String query) {


         log.info("User searched for {}", query);
             List<String> allQueries = new ArrayList<>();
            // Read the SFlogs.log file and extract the search queries
            try (BufferedReader br = new BufferedReader(new FileReader("M:/piSpring/welcome-to-esprit/evaluation-service/src/main/resources/SFlogs.log"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("User searched for ")) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 3) {
                            String q = parts[11];
                            allQueries.add(q);
                        }
                   }
                }
            } catch (IOException e) {
                // Handle exception
            }
            // Count the frequency of each search query
            Map<String, Long> queryCounts = allQueries.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            // Sort the search queries by frequency and return the top 10
            List<String> topQueries = queryCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        System.out.print("************ liste des 10 queries  *********");
        System.out.print("************ "+ topQueries +" *********");



         return faqRepository.search(query);



    }

    @Override
    public List<String> topSearchedQueries() {
        List<String> allQueries = new ArrayList<>();
        // Read the SFlogs.log file and extract the search queries
        try (BufferedReader br = new BufferedReader(new FileReader("M:/piSpring/welcome-to-esprit/evaluation-service/src/main/resources/SFlogs.log"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("User searched for ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3) {
                        String q = parts[11];
                        allQueries.add(q);
                    }
                }
            }
        } catch (IOException e) {
            // Handle exception
        }
        // Count the frequency of each search query
        Map<String, Long> queryCounts = allQueries.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Sort the search queries by frequency and return the top 10
        List<String> topQueries = queryCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.print("************ liste des 10 queries  *********");
        System.out.print("************ "+ topQueries +" *********");
        return topQueries ;

    }

    @Override
    public List<Faq> getDfaultFaqs() {

        List<Faq> defaultFaqs= new ArrayList<>() ;

        this.topSearchedQueries().forEach(
                query -> {
                    List<Faq> ListQuery=faqRepository.findByKeywords(query);
                    ListQuery.forEach(faq -> {
                        if(!defaultFaqs.contains(faq)) {
                            defaultFaqs.add(faq);
                        }
                    });

                }

        );

            return defaultFaqs ;
    }

    @Override
    public String reformuleResponse(String response) {
         return chatgptService.sendMessage(" return another new whole sentence having the same meaning as this one < :" + response + ">");
    }


}



