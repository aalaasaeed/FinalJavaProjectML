package com.example.FinalJavaProjectML;


import org.apache.commons.csv.CSVFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.type.StructType;
import smile.io.Read;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@RestController
public class SpringController {
    DataFrame df;
    List<Job> Jobs = new ArrayList<>();


    @GetMapping("/view")
    public String readData() throws IOException, URISyntaxException {

        String path="D:\\ITI - AI & Machine Learning\\18- Java For Machine Learning\\FinalJavaProjectML\\FinalJavaProjectML\\src\\main\\resources\\Wuzzuf_Jobs.csv";
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader ();
        this.df = Read.csv (path, format);

        ListIterator<Tuple> iterator = this.df.stream ().collect (Collectors.toList ()).listIterator ();
        System.out.println(this.df.summary ());
        System.out.println(this.df.schema ());
        int i=4380;
        while (iterator.hasNext () && i>0) {
            Tuple t = iterator.next ();
            Job p = new Job ((String)t.get ("Title"),(String)t.get ("Company"),(String)t.get ("Location"),"Type",
                    (String)t.get ("Level"),(String)t.get ("YearsExp"),(String)t.get ("Country"),(String)t.get ("Skills"));

            this.Jobs.add (p);
            i--;
        }
        String html = String.format("<h1 style=\"text-align:center;font-family:verdana;background-color:SpringGreen;\">%s</h1>", "Sample From The Data") +
                "<table style=\"width:100%;text-align: center\">" +
                "<tr><th>Title</th><th>Company</th><th>Location</th><th>Type</th><th>Level</th><th>YearsExp</th><th>Country</th><th>Skills</th></tr>";
        for (Job j:this.Jobs){
            html += "<tr>\n" +"<td>"+j.getTitle()+"</td>\n" +"<td>"+j.getCompany()+"</td>\n" +"<td>"+j.getLocation()+"</td>\n"
                    +"<td>"+j.getType()+"</td>\n" +"<td>"+j.getLevel()+"</td>\n" +"<td>"+j.getYears_EXP()+"</td>\n"+"<td>"+j.getCountry()+"</td>\n"+"<td>"+j.getSkills()
                    +"</td>\n"+"  </tr>";
        }

        return html;
    }

    @GetMapping("/Summary")
    public String getSummary() throws IOException, URISyntaxException {
        String r = readData();
        DataFrame summary = df.summary();
        String web = String.format("<h1 style=\"text-align:center;font-family:verdana;background-color:SpringGreen;\">%s</h1>", "Summary of The Data Set") +
                "<table style=\"width:100%;text-align: center\">" ;
        web += String.format("<h2 style=\"text-align:center;\"> Total Number of Records Before Cleaning = %d</h2>", df.stream().count());

        return web;
    }

    @GetMapping("/getSchema")
    public String getSchema() throws IOException, URISyntaxException {

        String r = readData();
        String []schemaa = df.schema().toString().replace("[","").replace("]","").split(",");

        String web = String.format("<h1 style=\"text-align:center;font-family:verdana;background-color:SpringGreen;\">%s</h1>", "Schema of Wuzzuf Data Set") +
                "<table style=\"width:100%;text-align: center\">" ;
        for (String s : schemaa){
            web+= String.format("<h2 style=\"text-align:center;\">  %s</h2>", s);
        }



        return web;
    }

    @GetMapping("/cleanData")
    public String cleanWuzzufData() throws IOException, URISyntaxException {
        String r = readData();
        DataFrame nonNullData= df.omitNullRows ();
        System.out.println ("Number of non Null rows is: "+nonNullData.nrows ());
        String web = String.format("<h1 style=\"text-align:center;font-family:verdana;background-color:SpringGreen;\">%s</h1>", "Data Cleaning") +
                "<table style=\"width:100%;text-align: center\">" ;
        web += String.format("<h2 style=\"text-align:center;\"> Total Number of Records Before Cleaning = %d</h2>", df.stream().count());
        web += String.format("<h2 style=\"text-align:center;\"> Total Number of Records Before Cleaning = %d</h2>", df.stream().count() - nonNullData.stream().count());


        return web;
    }


}

