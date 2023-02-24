package tn.esprit.springfever.configuration;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeoIpService {

    private DatabaseReader dbReader;
    private  DatabaseReader dbReader2;

    @PostConstruct
    public void init() throws IOException {
        File database = new File("C:/Users/aymen/Desktop/PI-spring_ang/welcome-to-esprit/user-service/src/main/resources/GeoLite2-City.mmdb");
        File database2 = new File("C:/Users/aymen/Desktop/PI-spring_ang/welcome-to-esprit/user-service/src/main/resources/GeoLite2-Country.mmdb");
        dbReader = new DatabaseReader.Builder(database).build();
        dbReader2 = new DatabaseReader.Builder(database2).build();
    }

    public String getCity(String ip) throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = dbReader.city(ipAddress);
        City city = response.getCity();
        return city.getName();
    }

    public String getCountry(String ip) throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CountryResponse response = dbReader2.country(ipAddress);
        Country country = response.getCountry();
        return country.getName();
    }

}

