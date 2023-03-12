package tn.esprit.springfever.Services.Interfaces;

import tn.esprit.springfever.entities.Claim;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ICsvExporter {

    public void exportClaimsToCsv(List<Claim> claims, HttpServletResponse response) throws IOException;

    }
