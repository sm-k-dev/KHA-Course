package test1;



//스마트폰을 설계하는 클래스(설계도)
public class Smartphone {
	
	//클래스 변수 
		String brand;  			//스마트폰 브랜드명
		String model;  			//스마트폰 모델명
		int  batteryCapacity;   //베터리 용량
	
	//생성자 
		//첫번째 생성자  - 모델명을 매개변수로 받을수 있는 생성자
		public Smartphone(String model) {
						//"Galaxy S24"
			
			//this() 다른생성자 호출구문을 사용해서  세번째 생성자 호출! 해서 객체 변수값들을 모두 초기화 할수 있게 하자
			this("Samsung", model, 4000);			
		}
		
		//두번째 생성자  - 브랜드명과 모델명을 매개변수로 받을수 있는 생성자
		public Smartphone(String brand,  String model) {
						 //  "Apple"       "iPhone 15"
			
			//this() 다른생성자 호출구문을 사용하여  세번째 생성자 호출! 해서 객체 변수값들을 모두 초기화 할수 있게 하자
			this(brand, model, 4000);		
		}
		
		//세번째 생성자  - 브랜드명과 모델명, 베터리용량 까지  매개변수로 받을 수 있는 생성자
		public Smartphone(String brand, String model,  int batteryCapacity) {
						    //"Google",    "Pixel 8",     4000
			
			//this 키워드를 사용하여  현재 객체 메모리의 모든 객체 변수값을 초기화
			this.brand = brand;
			this.model = model;
			this.batteryCapacity = batteryCapacity;
		}
		
			
	public static void main(String[] args) {
		
		Smartphone  phone1 = new Smartphone("Galaxy S24");
		
		System.out.println( phone1.brand  + ", " + phone1.model + ", " + phone1.batteryCapacity + "mAh");
							//Samsung, Galaxy S24, 4000mAh
					
		/*
							 -----------------0x1------------
							 
							 		//객체 변수 
									String brand = "Samsung";  			    //스마트폰 브랜드명
									String model = "Galaxy S24";   			//스마트폰 모델명
									int  batteryCapacity = 4000;            //베터리 용량
							 ------------------------------------
		*/
		
		Smartphone phone2 = new Smartphone("Apple", "iPhone 15");
		/*
							 -----------------0x2------------
							 
							 		//객체 변수 
									String brand = "Apple";  			    //스마트폰 브랜드명
									String model = "iPhone 15";   			//스마트폰 모델명
									int  batteryCapacity = 4000;            //베터리 용량
							 ------------------------------------
		 */
		System.out.println( phone2.brand  + ", " + phone2.model + ", " + phone2.batteryCapacity + "mAh");
		
		
		Smartphone phone3 = new Smartphone("Google", "Pixel 8", 4000);
		/*
							 -----------------0x2------------
							 
							 		//객체 변수 
									String brand = "Google";  			    //스마트폰 브랜드명
									String model = "Pixel 8";   			//스마트폰 모델명
									int  batteryCapacity = 4000;            //베터리 용량
							 ------------------------------------
		 */		
		System.out.println( phone3.brand  + ", " + phone3.model + ", " + phone3.batteryCapacity + "mAh");

	}

}












