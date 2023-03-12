package tn.esprit.springfever.Services.Implementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.ICsvExporter;
import tn.esprit.springfever.entities.Claim;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@Service

public class CsvExporter  implements ICsvExporter {



    public void exportClaimsToCsv(List<Claim> claims, HttpServletResponse response) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(Claim.class).withHeader();
        response.setHeader("Content-Disposition", "attachment; filename=claims.csv");
        response.setContentType("text/csv");
        csvMapper.writerFor(Claim.class)
                .with(csvSchema)
                .writeValues(response.getWriter())
                .writeAll(claims);
    }
}
