package ethirium.eth.scraper;

import ethirium.eth.controller.Ethirum;
import ethirium.eth.dto.CompanyDto;
import ethirium.eth.dto.ContactDto;
import ethirium.eth.dto.JobDto;
import ethirium.eth.service.CrawlerService;
import ethirium.eth.service.ScrapeService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Crawler {
//    private static FirefoxDriver driver = null;
//    private static String email="ishan2.eminda2@gmail.com";
//    public static String password="Emi123@fbc";

    @Autowired
    private ScrapeService scrapeService;

    public synchronized String getLinkItem(List urls) {
        if (urls.size() > 0)
            return (String) urls.remove(0);
        return null;
    }

    public synchronized Integer getLinkItemSize(List urls) {
        return urls.size();
    }

    public synchronized int getListSize(List urls) {
        return urls.size();
    }

    public static boolean jobCompletionMarked = false;

    public static boolean stop = false;

    public static AtomicInteger runCount = new AtomicInteger(-1);

    public void scrape(JobDto jobDto, List<String> urls, List<String> filters, Integer jobID, boolean getEmailOnly) throws InterruptedException, IOException {
        System.setProperty("webdriver.gecko.driver", "/var/lib/tomcat8/geckodriver");

        jobCompletionMarked = false;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FirefoxOptions options = new FirefoxOptions();
                options.setHeadless(false);
                FirefoxDriver driver = new FirefoxDriver(options);
                driver.get("https://app.snov.io/login");
                String url = null;
                int i = 0;
                try {
                    driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(jobDto.getEmail());
                    driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(jobDto.getPassword());
                    driver.findElement(By.xpath("//*[@id=\"buttonFormLogin\"]")).click();

                    List<CompanyDto> companyDtoList = new ArrayList<>();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int rowNum = 1;

                    L0:
                    while ((url = getLinkItem(urls)) != null) {
                        if (stop) {
                            break L0;
                        }
                        driver.get("https://app.snov.io/domain-search/" + url);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        WebElement span = null;
                        List<String> paths = new ArrayList<>();
//                        try { //geting company links
//                            span = driver.findElementByXPath("/html/body/div[2]/div/div[2]/div");
//                        } catch (Exception e) {
//                            i = 1;
//                        }
                        try {
                            //company name list retrival
                            span = driver.findElementByCssSelector("#domain-search").findElement(By.xpath("./div/div[2]"));

                            List<WebElement> divs = span.findElements(By.tagName("div"));

                            for (WebElement div : divs) {
                                paths.add(div.findElement(By.tagName("a")).getAttribute("href"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Row row;
                        int contactExtracted = 0;
                        if (paths.size() > 0) {
                            for (String path : paths) {
                                driver.get(path);
                                if (stop) {
                                    break L0;
                                }
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CompanyDto companyDto;
                                try {
                                    //Personal Contact Check
                                    if (driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/ul/li[1]/a")).getText().contains("Personal Contacts")) {
                                        String companyName = "";
                                        try {
                                            companyName = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div/div[1]/div/div[2]/h1")).getText().replace("Company", "");
                                        } catch (Exception n) {
                                            try {
                                                //if top is 2 , if top is 3 make this 2
                                                companyName = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/div/div/div[1]/div/div/h1")).getText().replace("Company", "");
                                            } catch (Exception e) {

                                            }
                                        }
                                        companyName += "\n";
                                        companyDto = new CompanyDto();

                                        companyDto.setCompanyName(companyName.split("\n")[1]);
                                        companyDto.setUrlSearched(url);
                                        Integer companyID = scrapeService.createCompany(companyDto, jobID);
                                        //making 100
                                        try {
                                            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[4]/div/div[2]/div[2]/label[3]/span")).click();
//                                            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[4]/div/div/div[21]/div[2]/label[3]/span")).click();
                                            Thread.sleep(6000);
                                        } catch (Exception e) {
                                        }
                                        int page = 0;
                                        L1:
                                        while (true) {
                                            try {
                                                //cookie accept button xpath
                                                driver.findElement(By.xpath("/html/body/div[5]/button")).click();
//                                                driver.findElement(By.xpath("/html/body/div[6]/button")).click();
                                            } catch (Exception e) {
                                            }
                                            try {
                                                Thread.sleep(6000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            List<WebElement> trs = null;
                                            try {
                                                //get table rows/html/body/div[2]/div/div/div[2]/div/div/div[4]/div/div/div[2]

                                                trs = driver.findElement(By.cssSelector(".columns_4")).findElements(By.cssSelector("div.table-flex__row"));
//                                                trs = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/div[1]/div[1]/table/tbody/tr"));
                                                trs.remove(0);
//                                                trs.remove(trs.size()-1);
//                                                trs.remove(trs.size()-1);
                                            } catch (Exception e) {
                                                break;
                                            }
                                            int index = 1;
                                            for (WebElement tr : trs) {
                                                if (stop) {
                                                    break L0;
                                                }
                                                ContactDto contactDto = new ContactDto();
//                                                System.out.println(tr.getAttribute("innerHTML"));
//                                    System.out.println(tr.getText());/html/body/div[2]/div/div/div[2]/div[1]/div[1]/table/tbody/tr[3]/td[2]/a/p
                                                List<WebElement> tds = tr.findElements(By.xpath("./div"));

                                                try {
                                                    contactDto.setName(tr.findElement(By.xpath("./div[2]/span[2]/span/a")).getText().trim());
                                                } catch (Exception d) {
                                                    System.out.println();
                                                }
//                                                contactDto.setName(driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[1]/div[1]/table/tbody/tr[" + index + "]/td[2]/a/p")).getText().trim());
                                                try {
                                                    //get first email only
                                                    contactDto.setEmail(tr.findElement(By.xpath("./div[3]/span[2]/div")).getText().split(" ")[0]);
//                                                    contactDto.setEmail(driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[1]/div[1]/table/tbody/tr[" + index + "]/td[3]/div/div/span[2]/span")).getText().split(" ")[0]);
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    contactDto.setRole(tr.findElement(By.xpath("./div[4]")).getText());
                                                } catch (Exception f) {
                                                    System.out.println();
                                                }
//                                                contactDto.setRole(driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div[1]/div[1]/table/tbody/tr[" + index + "]/td[4]")).getText());
                                                if (!checkWithFilters(contactDto.getRole(), filters)) {
                                                    continue;
                                                }
//                                    row = sheet.createRow(rowNum++);
//                                    row.createCell(0).setCellValue(urls.get(i));
//                                    row.createCell(1).setCellValue(companyDto.getCompanyName());
//                                    row.createCell(2).setCellValue(contactDto.getName());
//                                    row.createCell(3).setCellValue(contactDto.getEmail());
//                                    row.createCell(4).setCellValue(contactDto.getRole());
                                                index++;
                                                scrapeService.createContact(contactDto, companyID);
                                                contactExtracted++;
                                                companyDto.addContact(contactDto);

                                                System.out.println(contactDto);
                                            }
                                            break;

//                                            try {
//                                                //scroll down to click next button
//                                                driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//                                                Actions actions = new Actions(driver);
//
//                                                List<WebElement> nextMightBe = driver.findElements(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[4]/div/div/div[101]/div[1]/a"));
////                                                List<WebElement> nextMightBe = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[2]/div[1]/div[2]/div/div[1]/ul/li"));
//                                                for (WebElement element : nextMightBe) {
//                                                    if (element.getAttribute("class").toLowerCase().contains("arrow")) {
//                                                        actions.moveToElement(element).click().perform();
//                                                        System.out.println(url + ": page" + (++page));
//                                                        Thread.sleep(6000);
//                                                        continue L1;
//                                                    }
//                                                }
//                                                break L1;
//
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                                break;
//                                            }
                                        }
//                            fl.delete();
//                            workbook.write(fileOut);
                                        companyDtoList.add(companyDto);
                                    }
                                } catch (Exception r) {
                                    r.printStackTrace();
                                    continue;
                                }
                            }
                        }
                        if ((contactExtracted < 5 || paths.size() == 0) && getEmailOnly) {
                            driver.get("https://app.snov.io/domain-search/" + url);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (contactExtracted > 0 && paths.size() > 0) {
                                scrapeService.deleteUrlData(url, jobID);
                            }
                            List<WebElement> trs = driver.findElements(By.tagName("tr"));
                            CompanyDto companyDto = new CompanyDto();
                            companyDto.setUrlSearched(url);

                            Integer companyID = scrapeService.createCompany(companyDto, jobID);
                            int count = 0;
                            L1:
                            while (true) {
                                if (stop) {
                                    break L1;
                                }
                                trs = driver.findElements(By.tagName("tr"));
                                int k = 0;
                                for (WebElement tr : trs) {
                                    if (k++ < 2) {
                                        continue;
                                    }

                                    ContactDto contactDto = new ContactDto();
                                    contactDto.setEmail(tr.getText());
                                    companyDto.addContact(contactDto);
                                    count++;
                                    if (count >= 100) break L1;

                                    scrapeService.createContact(contactDto, companyID);
                                }
                                break;
//                                try {
//                                    driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//                                    Actions actions = new Actions(driver);
//
//                                    List<WebElement> nextMightBe = driver.findElements(By.xpath("/html/body/div["+(2+i)+"]/div[3]/div/div[1]/ul/li"));
////                                    List<WebElement> nextMightBe = driver.findElements(By.xpath("/html/body/div[4]/div[3]/div/div[1]/ul/li"));
//                                    for (WebElement element : nextMightBe) {
//                                        if (stop) {
//                                            break L1;
//                                        }
//                                        if (element.getText().toLowerCase().contains("next")) {
//                                            actions.moveToElement(element).click().perform();
////                                System.out.println(urls.get(i) + ": page" + (++page));
//                                            Thread.sleep(6000);
//                                            continue L1;
//                                        }
//                                    }
//                                    break L1;
//
//                                } catch (Exception e) {
////                                    e.printStackTrace();
//                                    break;
//                                }

                            }
                        }

                        scrapeService.saveCurrentStatus(url, jobID);
                    }
                    runCount.decrementAndGet();
                    if (!jobCompletionMarked && !stop && runCount.get() == 0) {
//                        scrapeService.markJobCompleted(jobID);
                        jobCompletionMarked = true;
                    } else if (stop) {
                        scrapeService.markJobStopped(jobID);
                    }
                    stop = false;
                    driver.close();
                } catch (WebDriverException rt) {
                    try {
                        rt.printStackTrace();
                        System.out.println("error occurred");
                        System.out.println(url);
                        Thread.sleep(10000);
//                        urls.add(url);
                        run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    System.out.println("exception happned");
                    System.out.println(url);
                    e.printStackTrace();
                    try {
                        driver.close();
                    } catch (WebDriverException r) {
                        r.printStackTrace();
                    }
                } finally {
                    stop = false;
                    CrawlerService.working = false;
                }
                if (getLinkItemSize(urls) == 0) {
//                    scrapeService.markJobCompleted(jobID);
                    jobCompletionMarked = true;
                }
            }
        };

        int num = urls.size() < 3 ? urls.size() : 3;
        runCount.set(num);
        ;
        for (int i = 0; i < num; i++) {
            new Thread(r).start();
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

        String columns[] = {"URL", "CompanyDto", "Name", "Email", "Role"};
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
