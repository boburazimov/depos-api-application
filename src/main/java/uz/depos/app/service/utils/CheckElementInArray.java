package uz.depos.app.service.utils;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class CheckElementInArray {

    public void checkInt(int[] arr, int toCheckValue) {
        // check if the specified element
        // is present in the array or not
        // using Linear Search method
        boolean test = false;
        for (int element : arr) {
            if (element == toCheckValue) {
                test = true;
                break;
            }
        }

        // Print the result
        System.out.println("Is " + toCheckValue + " present in the array: " + test);
    }

    public Boolean checkStr(List<String> arr, String toCheckValue) {
        // check if the specified element
        // is present in the array or not
        // using Linear Search method
        for (String element : arr) {
            if (Objects.equals(element, toCheckValue)) {
                return true;
            }
        }
        return false;
    }
}
