import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

// pamiętajcie aby mieć zainstalowane java sdk 1.8, oraz ustawione w Intellij:
// File > Project structure > Project > Project SDK > java 1,8 (jeżeli nie ma opcji to trzeba wskazać ścieżkę do java sdk na komputerze)
// File > Project structure > Project > Project language level > 8
// File > Project structure > Modules > language level > 8
// File > Settins > Build, Execution, Deployment > Compiler > Java compiler > Target bycode veriosn > 1,8

import java.util.List;

public class Registration extends TestBase {
    // https://www.red-gate.com/simple-talk/wp-content/uploads/imported/1269-Locators_table_1_0_2.pdf
    // https://www.red-gate.com/simple-talk/wp-content/uploads/imported/1269-Locators_groups_1_0_2.pdf?file=4938

    // przykład deklaracji pól metod, użyjemy potem ich do metody setUserData()
    private String UserName = "user" + getRandomNumber();
    private String UserMail = "mail" + getRandomNumber() + "@wp.pl";

    @Test
    public void test() {
        // otwieramy strone rejestracyjną
        driver.get("http://demoqa.com/registration/");


        // wykonujemy metodę setName i przekazujemy do niej 2 parametry: imię oraz nazwisko
        setName("Jan", "Kowalski");

        // wykonujemy metodę zaznaczającą losowwy status
        selectRandomMaritalStatus();

        // wykonujemy metodę, która zaznaczy hobby o podanym numerze (indeksy są liczone od liczone od 0)
        selectHobbyWithIndex(0);

        // wywołania odpowiednich metod + przekazanie ewentualnych parametrów
        selectCountry("Poland");
        setDates();
        setPhoneNuber("1234567890");

        // tutaj do setUserData przekazujemy UserName i UserMail, które są polami tej klasy
        setUserData(UserName, UserMail);

        // należy pamiętać aby w folderze drivers mieć obrazek o nazwie 'face' z rozszerzeniem .png
        addPhoto("C:\\drivers\\face.png");

        setPassword("qweasdzxc123");

        //znajdujemy element odpowiedzialny za zatwiedzenie rejestracji i klikamy go
        driver.findElement(By.name("pie_submit")).click();

        // znajdujemy element w którym wyświetla się informacja o powodzeniu lub niepowodzeniu rejestracji
        WebElement registrationMessage = driver.findElement(By.className("piereg_message"));

        // sprawdzamy asercją czy znaleziony element jest wyświetlony na stronie
        Assert.assertTrue(registrationMessage.isDisplayed());

        // sprawdzamy czy tekst w registrationMessage jest zgodny z oczekiwanym
        Assert.assertEquals(registrationMessage.getText(), "Thank you for your registration");
    }

    /*
     * Wszytkie poniższe metody mają nazwy, które ułatwiają zrozumienie co dana metoda robi
     * dodatkowo nazwy parametrów jakie przyjmują metody też są opisane w taki sposób
     * aby łatwo zrozumieć co przechowuje dana zmienna np firstName - imie jakie ustawi metoda.
     * Metody mają modifikator dostępu private bo są wykorzystywane jedynie w tej klasie
     */

    private void setName(String firstName, String secondName) {
        // sendKeysToElement to metoda która znajdziemy w klasie TestBase
        // odpowiada ona za znalezienie elementu o podanym w parametrze selektorze
        // oraz wstawienie do znalezionego elementu odpowiedniego tekstu
        sendKeysToElement(By.name("first_name"), firstName);
        sendKeysToElement(By.name("last_name"), secondName);
    }


    private void selectRandomMaritalStatus() {
        // znajdujemy listę elementów które spełniają dany selektor (w tym przypadku radio buttony statusów)
        List<WebElement> maritalStatuses =
                driver.findElements(By.cssSelector("input[class='input_fields  radio_fields']"));

        // uruchamiamy metode getRandomElement zadeklarowaną w klasie TestBase
        // przekazujemy do niej znalezioną wcześniej listę elementów,
        // następnie na zwróconym elemencie wykonujemy kliknięcie
        getRandomElement(maritalStatuses).click();
    }

    private void selectHobbyWithIndex(int index) {
        // znajdujemy listę hobby
        List<WebElement> hobbies =
                driver.findElements(By.cssSelector("input[class='input_fields  piereg_validate[required] radio_fields']"));

        // wybieramy jeden element z listy (element o ideksie równym temu, jaki przekazaliśmy do metody)
        // robimy to tylko po to aby w kolejnych linijkach kodu nie pisać cały czas 'hobbies.get(index)'
        WebElement radioButtonToSelect = hobbies.get(index);

        // w tym momencie zaznaczamy checkboxa, jednak nie możemy go zaznaczyć poprzez samo radioButtonToSelect.click()
        // najpierw musimy sprawdzić czy checkbox nie jest zaznaczony,
        // jeżeli checkbox byłby już zaznaczony, a następnie byśmy na niego kliknęli to został by odznaczony
        if (!radioButtonToSelect.isSelected()) {
            //klikamy w element jeżeli nie jest zaznaczony
            radioButtonToSelect.click();
        }
        // wywołujemy metodę z klasyTestBase, która wypisuje które hobby są zaznaczone a które nie
        printSelectedElements(hobbies);

        // sprawdzamy asercją czy na pewno element którego indeks przekazaliśmy został zaznaczony
        Assert.assertTrue(hobbies.get(index).isSelected());
    }


    private void selectCountry(String countryName) {
        // towrzymy obiekt Select - obługuje on listy rozwijane
        Select countries = new Select(driver.findElement(By.cssSelector("select[id='dropdown_7']")));

        // zaznaczamy opcję po wartości jej atrybutu 'value', która ma być równa parametrowi countryName
        countries.selectByValue(countryName);

        // getFirstSelectedOption() pobiera nam WebElement który jest aktualnie zaznaczony na liście,
        // następnie pobieramy z niego tekst przy pomocy .getText() i porównujemy go
        // z opcją którą przekazliśmy w parametrze metody
        Assert.assertEquals(countries.getFirstSelectedOption().getText(), countryName);
    }

    private void setDates() {
        // tutaj aby zoszczędzić troche miejsca tworzenie obiektu select przeniosłem do metody w klasie TestBase
        // następnie na zwróconym obiekcie Select wykonyjemy wybieranie opcji na 3 różne sposoby:

        // wybieranie po indeksie
        getSelect(By.cssSelector("select[id='mm_date_8']")).selectByIndex(3);

        // wybieranie po tekście widocznym w rozwijanej liście
        getSelect(By.cssSelector("select[id='dd_date_8']")).selectByVisibleText("10");

        //w wybieranie po 'value' => podobnie jak w metodzie selectCountry()
        getSelect(By.cssSelector("select[id='yy_date_8']")).selectByValue("2014");
    }

    // ta metoda obsługuje nam pole wstawiania pliku, wystarczy znaleźć napisać selektor do elementu obługującego
    // wstawianie plików, a następnie wysłać do tego elementu ściężkę do pliku na naszym komputerze
    // w ten sposób pomjamy konieczność przeklikiwania okienka z wskazywaniem pliku na komputerze
    private void addPhoto(String pathToPhoto) {
        sendKeysToElement(By.id("profile_pic_10"), pathToPhoto);
    }

    //poniższe metody wstawiają przekazany parametr/parametry do odpowiednich WebElementów
    private void setPhoneNuber(String phoneNumber) {
        sendKeysToElement(By.id("phone_9"), phoneNumber);
    }

    private void setUserData(String userName, String userMail) {
        sendKeysToElement(By.id("username"), userName);
        sendKeysToElement(By.id("email_1"), userMail);
    }

    private void setPassword(String password) {
        sendKeysToElement(By.id("password_2"), password);
        sendKeysToElement(By.id("confirm_password_password_2"), password);
    }
}