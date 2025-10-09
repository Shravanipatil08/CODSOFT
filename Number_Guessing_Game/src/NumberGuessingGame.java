import java.util.*;
public class NumberGuessingGame {
public static void main(String[] args) {
	Scanner sc= new Scanner (System.in);
	Random rand= new Random();
	
	int totalscore=0;
	boolean playAgain = true;
	
	System.out.println("Welcome to the NUMBER Guessing Game !");
	
	while(playAgain) {
		int randomNumber = rand.nextInt(100) + 1;
		int attempts=0;
		int maxAttempts = 7;
		boolean guessedCorrectly = false;
		
		System.out.println("\nThink number between 1 and 100.");
		System.out.println("You have "+maxAttempts+" attempts to guess it!");
		
		while(attempts < maxAttempts)
		{
			System.out.print("Enter your guess:");
			int guess = sc.nextInt();
			attempts++;
			
			if(guess == randomNumber)
			{
				System.out.println("Correct ! You guessed the number in "+attempts+" attempts !");
				guessedCorrectly = true;
				totalscore += (maxAttempts - attempts + 1)*10;
				break;
			}
			else if(guess > randomNumber)
			{
				System.out.println("Too high ! Try again.");
			}
			else
			{
				System.out.println("Too low ! Try again.");
			}
		}
			
			if(!guessedCorrectly)
			{
				System.out.println("You've used all attempts. The correct number was "+randomNumber);
			}
			
			System.out.println("Do you want to play another round? (yes/no):");
			String choice = sc.next().toLowerCase();
			
			if(!choice.equals("yes"))
			{
				playAgain =false;
			}
		}
		
		System.out.println("Game Over ! Your total score is: "+totalscore);
		System.out.println("Thankew for playing .... !");
		sc.close();
	}
}

