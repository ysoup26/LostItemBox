/*SecurityLora: 상자의 보안을 담당. 중앙 로라의 명령에 따라 수행(open or lcd)*/
#include <Arduino.h>
#include "SNIPE.h"
#include <SoftwareSerial.h>     
#include <Servo.h>
#include <LiquidCrystal_I2C.h>

#define TXpin 11
#define RXpin 10

LiquidCrystal_I2C lcd(0x27,16,2);

SoftwareSerial DebugSerial(RXpin,TXpin);
SNIPE SNIPE(Serial);
String lora_app_key = "11 22 33 44 55 66 77 88 99 aa bb cc dd ee ff 00";  

Servo ServoL;
Servo ServoR;

/*변수*/
int flag=1; //1: send, 0: do task
int i=1;
String v;
int c=1;
int ledG=3;
int servoL=9;//상자를 앞에서 봤을때 왼(9)오(8)
int servoR=8;

void setup() {
  Serial.begin(115200);
  DebugSerial.begin(115200);
 
  //서보모터 세팅 및 초기화
  ServoR.attach(servoR);  
  ServoL.attach(servoL);  
  //처음에 열려있음
  ServoR.write(90);
  ServoL.write(0);
  
  lcd.begin();
  lcd.clear();
  lcd.print("start");

  /* SNIPE LoRa Initialization */
  if (!SNIPE.lora_init()) {
    DebugSerial.println("SNIPE LoRa Initialization Fail!");
    while (1);
  }
  if (!SNIPE.lora_setAppKey(lora_app_key)) DebugSerial.println("SNIPE LoRa app key value has not been changed");
  if (!SNIPE.lora_setFreq(LORA_CH_1))DebugSerial.println("SNIPE LoRa Frequency value has not been changed");
  if (!SNIPE.lora_setSf(LORA_SF_7)) DebugSerial.println("SNIPE LoRa Sf value has not been changed");
  if (!SNIPE.lora_setRxtout(5000)) DebugSerial.println("SNIPE LoRa Rx Timout value has not been changed");
  
  DebugSerial.println("SNIPE LoRa Security start!");
}
void loop() {
  if(flag){//계속 명령 수신 받음
    //카메라쪽은 C# 코드를 사용
    v = SNIPE.lora_recv();//"S# C:cap or S:ON//깨지지 않게 짧게
    delay(300); 
    DebugSerial.println("#S recv wait");
//    //닫힘
//    ServoR.write(0);
//    ServoL.write(90);
//    delay(500);
//    ServoR.write(90);
//    ServoL.write(0);
//    
    Serial.println(v);
    Serial.println(v.indexOf("#S"));
    if(v.indexOf("#S")!=-1){
      flag=0;//명령에 따라 mode 변경
    }
  }else{//명령에 대한 수행
    //로라 수신 명령 분해
    v=v.substring(3);//실제 명령 부분만 사용함 # 이후부터
    //여러개 수행하기 위해 스위치문 사용
    int do_task;
    if(v.equals("OPEN"))
      do_task=1;
    else if(v.equals("CLOSE"))
      do_task=2;
    //실행 내용은 if-elseif가 길어지면 복잡하니 스위치문 사용
    switch(do_task)
    {
      case 1: //잠금장치 ON
        ServoR.write(0);
        ServoL.write(90);
        lcd.clear();
        lcd.setCursor(0,0); 
        lcd.print("BOX OPEN");

        break;
      case 2://잠금장치 OFF
        ServoR.write(0);
        ServoL.write(90);
        lcd.clear();
        lcd.setCursor(0,0); 
        lcd.print("BOX CLOSE");

        break;
    }
    //다 수행하고 중앙에게 ack 보냄
    if(SNIPE.lora_send("#C ACK")){ 
        Serial.println("#C ACK success");//신호받으면
        flag=1;
    }
  }
  
}
