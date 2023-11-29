package com.example.jcvbuilder.services;

import com.example.jcvbuilder.models.DTO.CVPayload;
import com.example.jcvbuilder.models.DTO.Experience;
import com.example.jcvbuilder.models.DTO.Skill;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CVBuilder {

    public static String generateExperienceListHtml(List<Experience> experiences) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<ul>");

        for (Experience experience : experiences) {
            htmlBuilder.append("<li>")
                    .append(formatDate(experience.startMonthYear))
                    .append(" - ")
                    .append(formatDate(experience.endMonthYear))
                    .append(": ")
                    .append(experience.jobDescription)
                    .append("</li>");
        }

        htmlBuilder.append("</ul>");
        return htmlBuilder.toString();
    }

    private static String formatDate(String monthYear) {
        String[] parts = monthYear.split("-");
        return parts[1] + "/" + parts[0];
    }

    public String generateExperienceHTML(CVPayload data){
        String htmlOutput = generateExperienceListHtml(data.experience);
        return htmlOutput;
    }

    public String generateSillsHTML(CVPayload data){
        StringBuilder htmlBuilder = new StringBuilder();

        for (Skill aSkill : data.skills) {
            htmlBuilder.append("<h3>").append(String.format("%s skills", aSkill.title)).append("</h3>");
            htmlBuilder.append("<table>")
                    .append("<tr><th>Skill</th><th>Level</th></tr>");

            for (Skill skill : aSkill.skills) {
                htmlBuilder.append("<tr>")
                        .append("<td>").append(skill.skill).append("</td>")
                        .append("<td>").append(skill.level).append("</td>")
                        .append("</tr>");
            }

            htmlBuilder.append("</table>");
        }

        return htmlBuilder.toString();
    }

    public void buildCV(CVPayload data){
        String pdfFilePath = "src/main/resources/templates/cvtemplate.html";
        File htmlTemplateFile  = new File(pdfFilePath);
        try{
            String htmlString = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
            String experiences = generateExperienceHTML(data);
            String skills = generateSillsHTML(data);

            htmlString = htmlString.replace("$fname", data.fullName);
            htmlString = htmlString.replace("$bdate", data.birthDate);
            htmlString = htmlString.replace("$mail", data.email);
            htmlString = htmlString.replace("$country", data.country);
            htmlString = htmlString.replace("$phone", data.phoneNumber);
            htmlString = htmlString.replace("$experience", experiences);
            htmlString = htmlString.replace("$skills", skills);
            File newHtmlFile = new File("src/main/java/com/example/jcvbuilder/resume_outputs/generated.html");
            FileUtils.writeStringToFile(newHtmlFile, htmlString, "UTF-8");

            this.htmlCVToPDF();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void htmlCVToPDF(){
        String htmlFilePath = "src/main/java/com/example/jcvbuilder/resume_outputs/generated.html";
        String outputPdf = "src/main/java/com/example/jcvbuilder/resume_outputs/generated.pdf";

        File htmlFile = new File(htmlFilePath);
        try{
            Document document = Jsoup.parse(htmlFile, "UTF-8");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(document.html());
            renderer.layout();
            try (OutputStream outputStream = new FileOutputStream(outputPdf)) {
                renderer.createPDF(outputStream);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
