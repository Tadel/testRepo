import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TestBase {
    // aby Intellij nie sprawdzał wam pisowni w komentarzach wejdźcie w
    // File >> Settings >> Editor >> Inspections >> Spelling >> Typo
    // i odznaczcie opcję 'process comments'

    // deklaracja webdrivera którego używamy w tej klasie oraz klasach które dziedziczą TestBase
    WebDriver driver;

    // setUp zostanie odpalone przed każdym teście dzięki adnotacji @BeforeMethod
    // odpowiada za wskazanie gdzie mamy pobrany chromedriver (pamiętaj aby u Ciebie był też w tej lokalizacji
    // pobierz najnowszą wersję https://sites.google.com/a/chromium.org/chromedriver/downloads
    // w powyższym liku widać też jaka wersja przeglądarki chrome jest wspierana przez daną wersję chromedrivera
    @BeforeMethod
    public void setUp() {
        // ustawianie ścieżki do do chromedrivera
        System.setProperty("webdriver.chrome.driver", "c:/drivers/chromedriver.exe");
        // inicjalizacja drivera + w tym momencie otwiera sie przeglądarka
        driver = new ChromeDriver();
        // maksymalizacja okna przeglądarki
        driver.manage().window().maximize();
    }

    // tearDown zostanie odpalone po każdym teście dzięki adnotacji @AfterMethod
    @AfterMethod
    public void tearDown() {
        // w bardzo brzydki sposób czekamy 2000ms = 2 sekundy Thread.sleep(2000), java wymaga wrzucenia tego w try catch
        // możecie usunąć try catch i zobaczyć co się stanie i jakie Intellij podpowie rozwiązania
        // jest te druga opcja poradzenia sobie z problemem i jej przykład napisałem w metodzie demoWait (kolejna metoda)
        try {
            // usypiamy program na 2 sekundy (dokładnie mówiąc to nie program a wątek, ale nie ma potrzeby sie tak zagłębiać w szczegóły)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //zamknięcie przeglądarki oraz zamknięcie procesu webdrivera
        driver.quit();
    }

    // tutaj metoda której nigdzie nie wywołujemy, ale chciałem pokazać, że mozemy nie robić try catch na Thread.sleep(2000);
    // a możemy też rozwiązać to poprzez dodanie sygnatury throws + exception do metody
    // dodałem to tylko jako ciekawostkę, chętni mogą zgłebić temat sygnatury throws, aczkolwiek nie będziemy z niej korzystać
    public void demoWait() throws InterruptedException {
        Thread.sleep(2000);
    }

    /*
     * poniżej metody które miały nam zaoszczędzić trochę miejsca w kodzie w naszej klasie testu rejestracji
     * przeniosłem je tutaj do klasy TestBase, żeby klasa Registration była bardziej czytelna
     * dzięki dziedziczeniu są one dostępne do wykorzystania w klasie Registration
     */

    // sendKeysToElement przyjmyje selektor by i testk do wstawienia
    public void sendKeysToElement(By by, String textToSet) {
        //znajdujemy element o przekazanym selektorze
        WebElement element = driver.findElement(by);

        // i wstawia do niego tekst przekazany do metody jako parametr
        element.sendKeys(textToSet);
    }


    public WebElement getRandomElement(List<WebElement> elementList) {
        // utworzenie obiektu służącego do generowania liczb losowych
        Random random = new Random();

        // losujemy numer z zakresu od 0 do wielkość tablicy -1 (odejmujemy 1 bo indeksy są od 0)
        int randomNumber = random.nextInt((elementList.size() - 1));

        // zwracamy element o ideksie równym wylosowanej liczbie
        return elementList.get(randomNumber);
    }

    // metoda zwracająca losowy numer z podanego zakresu
    public int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    // metoda zdwaracjąca nowy obiekt Selekt, służący do obsługi list rozwijanych
    // Select znajdywany jest po przekaznym selektorze
    public Select getSelect(By by) {
        return new Select(driver.findElement(by));
    }

    public void printSelectedElements(List<WebElement> list) {
        for (WebElement element : list) {
            if (element.isSelected()) {
                System.out.println(element.getAttribute("value") + " jest zaznaczony");
            } else {
                System.out.println(element.getAttribute("value") + " nie jest zaznaczony");
            }
        }
    }
}