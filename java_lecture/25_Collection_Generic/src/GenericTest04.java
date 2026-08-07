
import java.util.ArrayList;
import java.util.Vector;
/*
 제네릭 타입이란?
 	-  클래스를 설계 할 때  멤버변수,매개변수 등이 아직 결정되지 않는 자료형타입을 의미 합니다.
 	-  제네릭 타입은? 클래스의 객체메모리를 생성할때 제네릭 타입이 결정 되어 자동으로 작성됩니다.
 	

 제네릭 타입을 이용한 클래스 또는 인터페이스란?
 	-  멤버변수, 매개변수 타입(자료형)의 결정되어 있지 않는 클래스와 인터페이스를 말합니다.
*/
//주제 : 제네릭 기법이 작성된 클래스 실제 만들기 

class GenericClass<T> {  //<- <T> 의미 : GenericClass클래스 설계도 내부에는 아직 결정 되지 않는 제네렉타입을 가진 변수나 메소드 존재
	
	private  T   member; //현재 member 인스턴스변수는 아직 결정되지 않는 T 제네릭타입을 지정해서 만든 변수 
	
	public void setMember(T  value) {  //매개변수 value 또한 아직 결정되지 않는 T 제네릭타입 문자를 지정해서
							           //매개변수 value를 만들어 놓고 사용할수 있다.
		this.member = value;
	}
	
	//메소드의 반환 자료형 타입도 아직 결정되지 않은 T 제네릭 타입 문자를 지정해서 메소드를 만들어 놓고 사용할수 있다.



public T  getMember() {
		return this.member;
	}
	
}

//===================================================================================================

		//   <Tv, String>
		//   <Car, String>

class Product<K, V> { //<- <K,V> Product 클래스 설계도 내부에는 아직 결정되지 않은 제네릭 타입을 가진 변수나 메소드 존재!
					  //   <K,V> 제네렉 타입을 작성한 이유 : 다양한 종류의 모델 제품 저장하기 위해 제네릭 타입 지정!	
	
	//변수 
	private K kind;   //Tv    kind;
					  //Car   kind;
	
	private V model;  //String model;
					  //String model;
	
	//메소드
//  public Tv  getKind() {
//  public Car getKind() {	
	public K getKind() {
		
		return this.kind;
	}
	
//	public void setKind(Tv kind) {
//	public void setKind(Car kind) {	
	public void setKind(K  kind) {
		this.kind = kind;
	}
	
//	public String getModel() {
//	public String getModel() {
	public V      getModel() {
		return model;
	}

//	public void setModel(String model) {
//	public void setModel(String model) {
	public void setModel(V      model) {
		this.model = model;
	}	
	
} //-------------------------------- class Product<K, V>    클래스 끝

class Tv {}
class Car {}

//=======================================================================================

//Rentable 인터페이스를 제네릭 타입으로 선언해 보자
//다양한 대상을 렌트하기 위해 rent() 추상메소드의  반환자료형타입을 제네릭 타입으로 선언 합니다.
interface Rentable<P> {
	
	P rent(); //추상 메소드 
}

//렌트 대상인 Home 클래스와  Car2클래스 작성
class Home {
	public void turnOnLight() {
		System.out.println("전등을 켭니다.");
	}
}
class Car2 {
	public void run() {
		System.out.println("자동차가 달립니다.");
	}
}

//집 을 렌트 해주는 대리점 클래스
class HomeAgency implements Rentable{
	
	@Override
	public Home rent() {
		return new Home();
	}
}
//자동차를 렌트 해주는 대리점 클래스 
class CarAgency  implements Rentable{
	
	@Override
	public Car2 rent() {
		return new Car2();
	}
}
public class GenericTest04 {
	public static void main(String[] args) {
		
		//집을 렌트(대여) 해주는 대리점 역할의 HomeAgency클래스의 객체 생성
		HomeAgency   homeAgency = new HomeAgency();
		
		//대리점으로 부터 집을 렌트함
		Home home = homeAgency.rent();
		
		//렌트한 집으로 들어가서 전등을 켭니다.
		home.turnOnLight(); //"전등을 켭니다"
		
		//-------------------------------------------------------
		
		//자동차 렌트(대여) 해주는 대리점 역할의 CarAgency클래스의 객체 생성
		CarAgency  carAgency  =  new CarAgency();
		
		//자동차 렌트(대여) 하자
		Car2 car2 = carAgency.rent();
		
		//렌트한 자동으로 가서 달립니다.
		car2.run();  //"자동차가 달립니다"
		
		
		System.out.println("----------------------------------------------------------");
		
		
		//K 는 Car로 대체 , V는 String으로 대체 
		Product<Car, String> product2 = new Product<Car, String>();
		
							//setter 역할을 하는 메소드를 호출해서  Car제품 정보 저장
							product2.setKind(new Car());
							product2.setModel("SUV자동차");
							
							//getter 역할을 하는 메소드를 호출해서  인스턴스 변수 kind, model에 저장된 객체를 꺼내옵니다.
		Car		car	    =	product2.getKind();  //new Car();
		String carModel = 	product2.getModel(); //"SUV자동차"
							
						   //        new Car().equals( new Car() );
		boolean result	=	product2.getKind().equals( product2.getKind() );
		System.out.println(result); //true
					
					// 0x12 주소          0x13 주소  
					//"SUV자동차".equals("SUV자동차")
		if( product2.getModel().equals("SUV자동차")  ) {
				
			//->조건 해석 :두 문자열 객체 메모리의 주소번지 0x12 , 0x13 이 같으냐라고 물어보는 equals 메소드가 아니고! 
			//           두 문자열 객체 메모리 내부에 저장된 특정 문자열("SUV자동차"와 "SUV자동차") 자체가~ 같으냐?	
			
			System.out.println("두 문자열 객체 내부에 저장된 SUV자동차  문자열 2개가 같다.");  //<- 출력됨
			
		}else {
			System.out.println("두 문자열 객체 내부에 저장된 SUV자동차 문자열 2개는 같지 않다.");
		}
							
	
		
		System.out.println("-------------------------------------");
		
		//K는 Tv로  V자리에는 String으로 대체 
		Product<Tv, String>  product1 = new Product<Tv, String>();
		
							 product1.setKind(new  Tv());
							 product1.setModel("스마트TV");
		
		Tv		tv	    =	 product1.getKind();//  Tv kind 인스턴스변수에 저장된 -> new Tv(); 객체 주소 반환 받아 저장
		String	tvModel =    product1.getModel();// String model 인스턴스변수에 저장된 -> "스마트TV" 객체 주소 반환 받아 저장
		
		System.out.println(tv);  // Tv@279f2327
		System.out.println(tvModel); // 스마트TV
							 
		
		System.out.println("-----------------------------------");
		
     	//GenricClass<T> 클래스의 객체를 생성할때  <>오브 기호 안에 제네릭 타입을 작성해 놓으면
     	//GenricClass<T> 클래스 내부에 작성되어 있는 모든 변수( 인스턴스 변수 또는 매개변수 )나 메소드의 반환타입이 <>오브 기호 안에 적었던
     	//제네릭 타입 으로 변경 되어 다시 클래스가 만들어지며 객체가 생성 된다.
		GenericClass<Double> obj01 = new GenericClass<Double>();
		
							 obj01.setMember(3.5); //3.5는 자동(오토)박싱이 new Double(3.5); 래퍼객체로 전달됨 
//							 obj01.setMember(new Double(3.5));
							 	
		Double double1	 =	 obj01.getMember(); //new Double(3.5); 객체 주소번지 반환 
							 
		System.out.println(  double1.doubleValue()  );//new Double(3.5)객체 메모리에 저장된 기본자료형 3.5데이터를 꺼내오는 작업(수동 언박싱)
													  //수동 언박싱을 하기 위해 doubleValue()메소드를 호출하여 3.5데이터를 반환 받아 왔다.
										//3.5					 

		System.out.println("-----------------------------------");

		GenericClass<Integer>  obj02 = new GenericClass<Integer>();
		
							   obj02.setMember(  new Integer(10)   );
							   
							   System.out.println( obj02.getMember() ); 
							   					// new Integer(10); 반환받아
							   					// 오토언 박싱이 일어나  10을 꺼내와 10을 출력 
		System.out.println("---------------------------------------");
		
		//우리 개발가자 직접 만든 GenericClass<T> 처럼  Vector 클래스나 ArrayList 클래스 또한 제네릭 기법으로 설계 되어 있다.
		
		Vector<String> obj04 = new Vector<String>();
		
					   obj04.add("문자열1");  //<- add(E e) 메소드에서 add(String e) 로 변경됨
					   obj04.add("문자열2");
       				  //Vector 배열 전체 모습 
       				  //[ "문자열1" ][ "문자열2"   ][    ][    ][    ][    ][    ][    ][    ][    ]
       				  //  0     	     1         2     3     4     5      6     7    8     9    index
					   
					  System.out.println("Vector 배열에 저장된 객체 수  : " + obj04.size());
					  System.out.println("Vector 배열의 모든 칸 수  : " + obj04.capacity());
					  
		ArrayList<Integer> obj05 = new ArrayList<>();
						
						   obj05.add(new Integer(3));
						   obj05.add(new Integer(4));
						   obj05.add(5);
						   
		Integer	 integer = obj05.get(0);  //업캐스팅 X, 다운캐스팅 X 하지 않고 Integer클래스 자료형 변수에 반환받은 객체 저장
						   
		int      value	 = integer.parseInt("100"); //"100" -> 100
		
		System.out.println(value + 110); // 100 + 110  = 210
	
	}

}
	
	
	
	
	
