import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

// pamiętajcie aby mieć zainstalowane java sdk 1.8, oraz ustawione w Intellij:
// File > Project structure > Project > Project SDK > java 1,8 (jeżeli nie ma opcji to trzeba wskazać ścieżkę do java sdk na komputerze)
// File > Project structure > Project > Project language level > 8
// File > Project structure > Modules > language level > 8
// File > Settins > Build, Execution, Deployment > Compiler > Java compiler > Target bycode veriosn > 1,8

public class FirstTest extends TestBase {
    // przy wyszukiwaniu przycisków szukaj został użyty selektor nth-of-type(x)
    // ściągawkę do selektorów możecie znaleźć poniżej (obydwa linki zawierają ten sam spis selektorów tylko że są inaczej sformatowane)
    // https://www.red-gate.com/simple-talk/wp-content/uploads/imported/1269-Locators_table_1_0_2.pdf
    // https://www.red-gate.com/simple-talk/wp-content/uploads/imported/1269-Locators_groups_1_0_2.pdf?file=4938

    @Test
    public void standardSearch() {
        driver.get("https://www.google.com/");
        WebElement searchInput = driver.findElement(By.id("lst-ib"));
        searchInput.sendKeys("Adam Małysz");

        // do elementu możemy wysyłać pojedyńcze przyciski używając klasy Keys w metodzie sendKeys
        // poniższe wysłanie przycisku ENTER spowoduje wykonanie wyszukiwania bez potrzeby klikania w przycisk szukaj
        searchInput.sendKeys(Keys.ENTER);

        // sprawdzenie czy kolumna po prawej stronie wyszukiwania zawiera napis Adam Małysz
        WebElement searchResult = driver.findElement(By.cssSelector(".knowledge-panel span[data-original-name]"));
        Assert.assertEquals(searchResult.getText(), "Adam Małysz");
    }

    @Test
    public void luckySearch() {
        driver.get("https://www.google.com/");
        WebElement searchInput = driver.findElement(By.id("lst-ib"));
        searchInput.sendKeys("Adam Małysz");

        // po wpisaniu 'Adam Małysz' wyświetla się nam lista która zasłania nam 2 przyciski
        // możemy ten problem łatwo ominąć, po wpisaniu tekstu w pole wyszukiwania
        // klikamy w dowolny element na stronie i lista się zamknie
        // poniższa linia kodu odnajduje na stronie logo i w nie klika
        driver.findElement(By.id("hplogo")).click();

        // po kliknięciu w logo nic nie przysłania przycisków i możemy w nie bez problemu klikać
        // pobranie przycisku "szczęśliwy traf" i kliknięcie go
        WebElement searchButton = driver.findElement(By.cssSelector("input[type='submit']:nth-of-type(2)"));
        searchButton.click();
    }

    // enabled = false podowuje, że dany test jest ignorowany podczas uruchamiania testów
    @Test(enabled = false)
    public void disabledTest(){
        Assert.assertEquals(1,1);
    }
}