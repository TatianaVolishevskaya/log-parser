package logParser;

import java.util.Scanner;

public class Main {
    public static void main (String[] args){
        System.out.println("Введите число: ");
        int number = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число : ");
        int number2 = new Scanner(System.in).nextInt();
        int firstNumber = number;
        int secondNumber = number2;
        System.out.println("Сумма: "+ (number+number2));
        System.out.println("Разность: " + (number-number2));
        System.out.println("Произведение: "+ number*number2);
        System.out.println("Частное: " + (double)number/number2);
    }
}
