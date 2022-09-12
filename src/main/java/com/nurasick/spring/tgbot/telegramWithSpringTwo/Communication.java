package com.nurasick.spring.tgbot.telegramWithSpringTwo;

import com.nurasick.spring.tgbot.telegramWithSpringTwo.entity.CryptoData;
import com.nurasick.spring.tgbot.telegramWithSpringTwo.utils.RestTemplateModified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Communication {
    @Autowired
    RestTemplateModified restTemplate;
    private final String URL = "https://cex.io/api/last_price/";

    public Communication() {
//        setup();
    }

    // doesnot work
    public Communication(RestTemplateModified restTemplate) {
        this.restTemplate = restTemplate;
//        setup();
    }

/*    public void setup() {
        //https://stackoverflow.com/questions/44176335/restclientexception-could-not-extract-response-no-suitable-httpmessageconverte
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }*/


    public CryptoData getLastPrice(String currency1, String currency2) {
        return restTemplate.getForObject(URL + currency1 + "/" + currency2, CryptoData.class);
    }

    public void test() {
        System.out.println(restTemplate.getForObject("https://cex.io/api/last_price/BTC/USD", CryptoData.class));
    }

    public double getPrice() {
        return restTemplate.getForObject("https://cex.io/api/last_price/BTC/USD", CryptoData.class).getLprice();
    }

    public void showPrice(int seconds) {
        CryptoData cryptoData;
        LocalTime time;
//        Date date = new Date();

        for(int i = 0; i < seconds; i++) {
            cryptoData = restTemplate.getForObject("https://cex.io/api/last_price/BTC/USD", CryptoData.class);
            time = LocalTime.now();
            System.out.println(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " +
                    cryptoData.getCurr1() + "/" + cryptoData.getCurr2() + ": " + cryptoData.getLprice());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
