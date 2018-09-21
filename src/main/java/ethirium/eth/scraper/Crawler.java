package ethirium.eth.scraper;

import ethirium.eth.controller.Ethirum;
import ethirium.eth.dto.Company;
import ethirium.eth.dto.Contact;
import ethirium.eth.dto.JobDto;
import ethirium.eth.service.ScrapeService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Crawler {
//    private static FirefoxDriver driver = null;
//    private static String email="ishan2.eminda2@gmail.com";
//    public static String password="Emi123@fbc";

    @Autowired
    private ScrapeService scrapeService;

    public void scrape(JobDto jobDto, List<String> urls, List<String> filters, Integer jobID) throws InterruptedException, IOException {
        System.setProperty("webdriver.gecko.driver", "/var/lib/tomcat8/geckodriver");
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);

        new File("./content").mkdir();
        File fl = new File("./content/" + jobID + "_scrape.xlsx");
        FileOutputStream fileOut = new FileOutputStream("./content/" + jobID + "_scrape.xlsx");
//        fl.createNewFile();

        FileInputStream fsIP = new FileInputStream(fl);
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
//        workbook.write(fileOut);
        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        FirefoxDriver driver = new FirefoxDriver();
        driver.get("https://app.snov.io/login");

        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(jobDto.getEmail());
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(jobDto.getPassword());
        driver.findElement(By.xpath("//*[@id=\"buttonFormLogin\"]")).click();

        List<Company> companyList = new ArrayList<>();

        Thread.sleep(200);

//        String []urls={"matholdingsinc.com","pottyboss.com"};
        try {
            for (int i = 0; i < urls.size(); i++) {
                driver.get("https://app.snov.io/domain-search/" + urls.get(i));

                WebElement span = driver.findElementByXPath("/html/body/div[3]/div[2]/p[1]/span[2]");

                List<WebElement> aTagList = span.findElements(By.tagName("a"));
                List<String> paths = new ArrayList<>();

                for (WebElement element : aTagList) {
                    paths.add(element.getAttribute("href"));
                }


                Sheet sheet = createSheet(urls.get(i), workbook);
                int rowNum = 1;
                Row row;
                if (paths.size() > 0) {
                    for (String path : paths) {
                        driver.get(path);
                        Thread.sleep(100);
                        Company company;
                        if (driver.findElement(By.xpath("/html/body/div[2]/div/div/ul/li[1]/a")).getText().contains("Personal Contacts")) {
                            String companyName = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div/div[1]/div/div/h1")).getText();
                            company = new Company();

                            company.setCompanyName(companyName.split("\n")[1]);


                            company.setUrlSearched(urls.get(i));

                            try {
                                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div[1]/div[2]/div/div[2]/div/label[3]")).click();
                                Thread.sleep(6000);
                            } catch (Exception e) {
                            }

                            while (true) {
                                List<WebElement> trs = driver.findElements(By.xpath("/html/body/div[2]/div/div/div[2]/div[1]/div[1]/table/tbody/tr"));
                                int index = 1;
                                for (WebElement tr : trs) {
                                    Contact contact = new Contact();
                                    List<WebElement> tds = tr.findElements(By.tagName("td"));
                                    contact.setName(tds.get(2).getText().trim());
                                    try {
                                        contact.setEmail(driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div[1]/div[1]/table/tbody/tr[" + index + "]/td[4]/div/div[1]/span[2]")).getText().split(" ")[0]);
                                    } catch (Exception e) {
                                    }
                                    contact.setRole(tds.get(4).getText());
                                    if (!checkWithFilters(contact.getRole(), filters)) {
                                        continue;
                                    }
                                    row = sheet.createRow(rowNum++);
                                    row.createCell(0).setCellValue(urls.get(i));
                                    row.createCell(1).setCellValue(company.getCompanyName());
                                    row.createCell(2).setCellValue(contact.getName());
                                    row.createCell(3).setCellValue(contact.getEmail());
                                    row.createCell(4).setCellValue(contact.getRole());
                                    index++;

                                    company.addContact(contact);

                                    System.out.println(contact);
                                }

                                try {
//                            Thread.sleep(4000);
//                            last_height = driver.executeScript("return document.body.scrollHeight");
                                    driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                                    Actions actions = new Actions(driver);

                                    actions.moveToElement(driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div[1]/div[2]/div/div[1]/ul/li[12]/a"))).click().perform();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
//                            fl.delete();
//                            workbook.write(fileOut);
                        companyList.add(company);
                        }
                    }
                } else {
                    List<WebElement> trs = driver.findElements(By.tagName("tr"));
                    Company company = new Company();
                    company.setUrlSearched(urls.get(i));
                    int k = 0;
                    for (WebElement tr : trs) {
                        if (k++ < 2) {
                            continue;
                        }
                        row = sheet.createRow(rowNum++);
                        ;
                        Contact contact = new Contact();
                        contact.setEmail(tr.getText());
                        company.addContact(contact);

                        row.createCell(0).setCellValue(urls.get(i));
//                row.createCell(1).setCellValue(company.getCompanyName());
//                row.createCell(2).setCellValue(contact.getName());
                        row.createCell(3).setCellValue(contact.getEmail());
//                row.createCell(5).setCellValue(contact.getRole());
                    }
//                    fl.delete();
//                    workbook.write(fileOut);
//                companyList.add(company);
                }

                for (int j = 0; j < 5; j++) {
                    sheet.autoSizeColumn(j);
                }

                scrapeService.saveCurrentStatus(urls.get(i), jobID);
            }
            scrapeService.markJobCompleted(jobID);
//            fl.delete();
        workbook.write(fileOut);
//            fileOut.close();

            // Closing the workbook
            workbook.close();
        } finally {
//            fl.delete();
//            workbook.write(fileOut);
            fileOut.close();
            fsIP.close();
//            workbook.close();
        }

    }

    private boolean checkWithFilters(String role, List<String> filters) {
        if (filters.size() > 0) {
            for (String filter : filters) {
                if (role.toLowerCase().contains(filter)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }


    private Sheet createSheet(String domain, Workbook workbook) {
        Sheet sheet = workbook.createSheet(domain);
        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        String columns[] = {"URL", "Company", "Name", "Email", "Role"};
        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        return sheet;
    }

    public synchronized static Ethirum retrieve(String hpSent, String wattSent, String costSent) throws InterruptedException {


        Ethirum ethirum = new Ethirum();
////        System.out.println("sd");
////        driver.navigate().refresh();
////        System.out.println(driver.getPageSource());
//        WebElement hp = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[1]/div/div[2]/div/div[1]/div[2]/input");
//        hp.clear();
//        hp.sendKeys(hpSent);
//        Thread.sleep(100);
//
//        WebElement watt = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[1]/div/div[2]/div/div[2]/div[2]/input");
//        watt.clear();
//        watt.sendKeys(wattSent);
//        Thread.sleep(100);
//
//        WebElement cost = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[1]/div/div[2]/div/div[3]/div[2]/input");
//        cost.clear();
//        cost.sendKeys(costSent);
//        Thread.sleep(100);
//
//        // Getting values
//        WebElement profitRate = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[1]/div/div[2]/span[1]");
//        ethirum.setProfitRatePerDay(profitRate.getText() + "%");
//
//        WebElement monthlyProfit = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/span[2]");
//        ethirum.setMonthlyProfit("$" + monthlyProfit.getText());
//
//        //Day Profit
//        EthirumProfit day = new EthirumProfit();
//        WebElement profit = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[3]/div/div[1]/div[2]/span[2]");
//        day.setProfit("$" + profit.getText());
//
//        WebElement eth = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[3]/div/div[2]/div[1]/div[2]/span[2]");
//        day.setEthirium(eth.getText());
//
//        WebElement costVal = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[3]/div/div[2]/div[2]/div[2]/span[2]");
//        day.setCost(costVal.getText());
//        ethirum.setDayProfit(day);
//
//        //Week Profit
//        EthirumProfit week = new EthirumProfit();
//        profit = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[4]/div/div[1]/div[2]/span[2]");
//        week.setProfit("$" + profit.getText());
//
//        eth = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[4]/div/div[2]/div[1]/div[2]/span[2]");
//        week.setEthirium(eth.getText());
//
//        costVal = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[4]/div/div[2]/div[2]/div[2]/span[2]");
//        week.setCost(costVal.getText());
//        ethirum.setWeekProfit(week);
//
//        //Month profit
//        EthirumProfit month = new EthirumProfit();
//        profit = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[5]/div/div[1]/div[2]/span[2]");
//        month.setProfit("$" + profit.getText());
//
//        eth = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[5]/div/div[2]/div[1]/div[2]/span[2]");
//        month.setEthirium(eth.getText());
//
//        costVal = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[5]/div/div[2]/div[2]/div[2]/span[2]");
//        month.setCost(costVal.getText());
//        ethirum.setMonthProfit(month);
//
//        //Year Profit
//        EthirumProfit year = new EthirumProfit();
//        profit = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[6]/div/div[1]/div[2]/span[2]");
//        year.setProfit("$" + profit.getText());
//
//        eth = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[6]/div/div[2]/div[1]/div[2]/span[2]");
//        year.setEthirium(eth.getText());
//
//        costVal = driver.findElementByXPath("/html/body/div[1]/div[4]/div[7]/div/div/div[2]/div/div/div[2]/div[6]/div/div[2]/div[2]/div[2]/span[2]");
//        year.setCost(costVal.getText());
//        ethirum.setYearProfit(year);


        return ethirum;


    }

    public static void main(String[] args) throws InterruptedException, IOException {
//        initialise();
    }

}
