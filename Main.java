
// simple web-crawling demo :)
/*
A mashup of code in the wild (eg. from the crawler4j repo etc.) and a bit of my own...

This is a minimal example, comprised of just 2 source files, 2 libs/ .jar files...
*/

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class Main {
    static final String CRAWL_STORAGE_FOLDER = "D:\\code\\USC_Courses\\572\\HW2\\src\\main\\resources\\";
    static final int POLITENESS_DELAY = 100;
    static final int MAX_DEPTH = 16;
//    static final int MAX_PAGES = 300;
    static final int MAX_PAGES = 20000;
    static final boolean INCLUDE_BINARY_CONTENT  = true;
    static final boolean RESUMABLE_CRAWLING  = false;
    static final String HTTPS_USATODAY = "https://www.usatoday.com/";
    static final String HTTP_USATODAY = "http://www.usatoday.com/";
    static final String NEWSSITE_NAME = "usatoday";
    static final int CRAWLER_NUMBER  = 16;
    static final int SOCKET_TIMEOUT = 10000;
    static final int CONNECTION_TIMEOUT = 1000;
    // Proxy
    static final String PROXY_HOSTNAME = "proxy.apify.com";
    static final int PROXY_PORT = 8000;
    static final String PROXY_USERNAME = "groups-BUYPROXIES94952";
    static final String PROXY_PASSWORD = "apify_proxy_OqGdROBSCrgnWmeYGlzS1Y1I5m3GFJ4hGYdg";




    public static void main(String[] args) throws Exception {

        // for our crawling specs 
        CrawlConfig config = new CrawlConfig();

        // Set the folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        config.setCrawlStorageFolder(CRAWL_STORAGE_FOLDER);

        config.setRespectNoIndex(false);
        config.setSocketTimeout(SOCKET_TIMEOUT);
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
//        config.setProxyHost(PROXY_HOSTNAME);
//        config.setProxyPort(PROXY_PORT);
//        config.setProxyUsername(PROXY_USERNAME);
//        config.setProxyPassword(PROXY_PASSWORD);


        // Be polite: Make sure that we don't send more than 1 request per second (1000 milliseconds between requests).
        // Otherwise it may overload the target servers.
        config.setPolitenessDelay(POLITENESS_DELAY);

        // You can set the maximum crawl depth here. The default value is -1 for unlimited depth.
        config.setMaxDepthOfCrawling(MAX_DEPTH);

        // You can set the maximum number of pages to crawl. The default value is -1 for unlimited number of pages.
        config.setMaxPagesToFetch(MAX_PAGES);

        // Should binary data should also be crawled? example: the contents of pdf, or the metadata of images etc
        config.setIncludeBinaryContentInCrawling(INCLUDE_BINARY_CONTENT);

        // Do you need to set a proxy? If so, you can use:
        // config.setProxyHost("proxyserver.example.com");
        // config.setProxyPort(8080);

        // If your proxy also needs authentication:
        // config.setProxyUsername(username); config.getProxyPassword(password);

        // This config parameter can be used to set your crawl to be resumable
        // (meaning that you can resume the crawl from a previously
        // interrupted/crashed crawl). Note: if you enable resuming feature and
        // want to start a fresh crawl, you need to delete the contents of
        // rootFolder manually.
        config.setResumableCrawling(RESUMABLE_CRAWLING);

        // Set this to true if you want crawling to stop whenever an unexpected error
        // occurs. You'll probably want this set to true when you first start testing
        // your crawler, and then set to false once you're ready to let the crawler run
        // for a long time.
        //config.setHaltOnError(true);

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


        // STARTER 'seed'      
        controller.addSeed(HTTPS_USATODAY);
        controller.addSeed(HTTP_USATODAY);
//        controller.addSeed("https://www.gannett-cdn.com/presto/2023/01/17/USAT/f38c810a-90fc-478a-8d95-dd2fc88ff5a3-2which-art-museums-gets-your-vote.jpg?crop=1279%2C720%2Cx0%2Cy64&width=660&height=370&format=pjpg&auto=webp");


        // Number of threads to use during crawling. Increasing this typically makes crawling faster. But crawling
        // speed depends on many other factors as well. You can experiment with this to figure out what number of
        // threads works best for you.
//        int numberOfCrawlers = CRAWLER_NUMBER;

        // To demonstrate an example of how you can pass objects to crawlers, we use an AtomicInteger that crawlers
        // increment whenever they see a url which points to an image.
        //AtomicInteger numSeenImages = new AtomicInteger();

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        //controller.start(factory, numberOfCrawlers);
        // GO!!!!
        long startTime = System.currentTimeMillis();
        controller.start(BasicCrawler.class, CRAWLER_NUMBER);
        long endTime = System.currentTimeMillis();
        System.out.println("Crawling time cost: " + (endTime - startTime) / (1000 * 60) + "mins");

        DataWriter.writeURLStatus(Data.urlStatus,CRAWL_STORAGE_FOLDER +
                "fetch_"+ NEWSSITE_NAME +".csv");
        DataWriter.writeURLDiscovered(Data.urlDiscovered, CRAWL_STORAGE_FOLDER +
                "visit_"+ NEWSSITE_NAME +".csv");
        DataWriter.writeURLSuccess(Data.urlContentType, Data.urlPageSize,
                Data.urlOutlinkSize, CRAWL_STORAGE_FOLDER +
                "urls_"+ NEWSSITE_NAME +".csv");
    }

}// Main