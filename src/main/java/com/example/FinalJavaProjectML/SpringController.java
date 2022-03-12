package com.example.FinalJavaProjectML;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.io.Read;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;
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
            Job p = new Job ((String)t.get ("Title"),(String)t.get ("Company"),(String)t.get ("Location"),(String)t.get ("Type"),
                    (String)t.get ("Level"),(String)t.get ("YearsExp"),(String)t.get ("Country"),(String)t.get ("Skills"));

            this.Jobs.add (p);
            i--;
        }
        String html = String.format("<h1 style=\"text-align:center;font-family:verdana;background-color:FF8AAE;\">%s</h1>", "Sample of Wuzzuf Data ") +
                "<table style=\"width:100%;text-align: center ; border: 1px solid;\"> <br><br>" +
                "<tr style = \"border: 1px solid\"><th style = \"border: 1px solid\">Title</th ><th style = \"border: 1px solid\">Company</th><th style = \"border: 1px solid\">Location</th style = \"border: 1px solid\"><th style = \"border: 1px solid\">Type</th><th style = \"border: 1px solid\">Level</th><th style = \"border: 1px solid\">YearsExp</th><th style = \"border: 1px solid\">Country</th><th style = \"border: 1px solid\">Skills</th></tr>";
        for (Job j:this.Jobs){
            html += "<tr style = \"border: 1px solid\">\n" +"<td style = \"border: 1px solid\">"+j.getTitle()+"</td>\n" +"<td style = \"border: 1px solid\">"+j.getCompany()+"</td>\n" +"<td style = \"border: 1px solid\">"+j.getLocation()+"</td>\n"
                    +"<td style = \"border: 1px solid\">"+ j.getType() +"</td>\n" +"<td style = \"border: 1px solid\">"+j.getLevel()+"</td>\n" +"<td style = \"border: 1px solid\">"+j.getYears_EXP()+"</td>\n"+"<td style = \"border: 1px solid\">"+j.getCountry()+"</td>\n"+"<td style = \"border: 1px solid\">"+j.getSkills()
                    +"</td>\n"+"  </tr>";
        }

        return html;
    }

    @GetMapping("/describe")
    public String getSummary() throws IOException, URISyntaxException {
        String r = readData();
        String []schemaa = df.schema().toString().replace("[","").replace("]","").split(",");

        String web = String.format("<h1 style=\"text-align:center;font-family:verdana;background-color:FF8AAE;\">%s</h1>", "Summary and Scehma of Wuzzuf Data ") +
                "<table style=\"width:100%;text-align: center\"> <br> <br>" ;
        web += String.format("<h3 style=\"text-align:center;\"> Total Number of Records in Wuzzuf Data = %d</h3>", df.stream().count());
        web += "<h2 style=\"text-align:center;\"> Schema of Wuzzuf Data  </h2>";
        for (String s : schemaa){
            web+= String.format("<h2 style=\"text-align:center;\">  %s</h2>", s);
        }

        return web;
    }

    @GetMapping("/cleanData")
    public String cleanWuzzufData() throws IOException, URISyntaxException {
        String r = readData();
        DataFrame nonNullData= df.omitNullRows ();
        List<Tuple> withoutDupes = df.stream().distinct().collect(Collectors.toList());
        System.out.println ("Number of non Null rows is: "+nonNullData.nrows ());
        String web = String.format("<h1 style=\"text-align:center;font-family:verdana;background-color:FF8AAE;\">%s</h1>", "Data Cleaning") +
                "<table style=\"width:100%;text-align: center\">" ;
        web += String.format("<h2 style=\"text-align:center;\"> Total Number of Records Before Removing Null values = %d</h2>", df.stream().count());
        web += String.format("<h2 style=\"text-align:center;\"> Total Number of Records After Removing Null values  = %d</h2>", df.stream().count() - nonNullData.stream().count());
        web+=String.format("<h2 style=\"text-align:center;\"> Total records = %d</h2>", withoutDupes.size() );

        return web;
    }

    public LinkedHashMap<String, Integer> SortByValue (HashMap<String, Integer> ToSortCompany) {
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(ToSortCompany.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> es1, Map.Entry<String, Integer> es2) {
                return es2.getValue().compareTo(es1.getValue());
            }
        });
        LinkedHashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public List<Job> GetAllData() {
        List<Job> AllJobs=new ArrayList<>();
        ListIterator<Tuple> iterator = df.stream ().collect (Collectors.toList ()).listIterator ();
        List<Tuple> withoutDupes = df.stream().distinct().collect(Collectors.toList());
        int i = withoutDupes.size();
        while (iterator.hasNext () && i>0) {
            Tuple t = iterator.next ();
            Job p = new Job ((String)t.get ("Title"),(String)t.get ("Company"),(String)t.get ("Location"),(String)t.get ("Type"),
                    (String)t.get ("Level"),(String)t.get ("YearsExp"),(String)t.get ("Country"),(String)t.get ("Skills"));
            AllJobs.add (p);
            i--;
        }
        return AllJobs;
    }







    public void Showpiechart (List<String> companies, List<Integer> counts) {

        PieChart chart = new PieChartBuilder().width(1500).height(900).title("Count the jobs for each company (Pie Chart )").build();
        int limit = 25;
        for (int i = 0; i < limit; i++) {
            chart.addSeries(companies.get(i), counts.get(i));
        }
        try {
            BitmapEncoder.saveBitmapWithDPI(chart, "./PieChart_compaines", BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showJobsTitleBarChart(List<String> jobTitles, List<Integer> counts){

        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("JobsTitles").xAxisTitle("Job Titles").yAxisTitle("Count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.addSeries ("Counts Of Job Titles ",jobTitles.stream().limit(15).collect(Collectors.toList()), counts.stream().limit(15).collect(Collectors.toList()));
        try {
            BitmapEncoder.saveBitmapWithDPI(chart, "./barChart_titles", BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public void showAreasBarChart(List<String> Areas, List<Integer> counts){

        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Areas").xAxisTitle("Areas").yAxisTitle("Count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.addSeries ("Counts Of Areas ",Areas.stream().limit(15).collect(Collectors.toList()), counts.stream().limit(15).collect(Collectors.toList()));
        try {
            BitmapEncoder.saveBitmapWithDPI(chart, "./barchart_areas", BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public void showMostRepeatedSkillsPieChart(List<String> skills, List<Integer> counts) {

        PieChart chart = new PieChartBuilder().width(1500).height(900).title("Most Repeated Skills  (Pie Chart )").build();
        int limit = 25;
        for (int i = 0; i <= limit; i++) {
            chart.addSeries(skills.get(i), counts.get(i));
        }
        try {
            BitmapEncoder.saveBitmapWithDPI(chart, "./PieChart_skills", BitmapEncoder.BitmapFormat.PNG, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  @GetMapping("/piechart1")
    public ResponseEntity<byte[]> getpieChart_1() {

        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File("D:\\ITI - AI & Machine Learning\\18- Java For Machine Learning\\FinalJavaProjectML\\PieChart_compaines.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
    @GetMapping("/barchart1")
    public ResponseEntity<byte[]> getBarChart_1() {

        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File("D:\\ITI - AI & Machine Learning\\18- Java For Machine Learning\\FinalJavaProjectML\\barChart_titles.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
    @GetMapping("/barchart2")
    public ResponseEntity<byte[]> getBarChart_2() {

        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File("D:\\ITI - AI & Machine Learning\\18- Java For Machine Learning\\FinalJavaProjectML\\barchart_areas.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
    @GetMapping("/piechart2")
    public ResponseEntity<byte[]> getPieChart_2() {

        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File("D:\\ITI - AI & Machine Learning\\18- Java For Machine Learning\\FinalJavaProjectML\\PieChart_skills.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }


}

