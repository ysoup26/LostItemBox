/*BoxLora: 분실물을 감지하고 중앙로라에 센서 값 전송*/
#include <SoftwareSerial.h>
#include "SNIPE.h"

#define TXpin 11
#define RXpin 10

SoftwareSerial DebugSerial(RXpin,TXpin);
SNIPE SNIPE(Serial);
String lora_app_key = "11 22 33 44 55 66 77 88 99 aa bb cc dd ee ff 00";  

/*변수*/ 
const int trigPinR = 2;
const int echoPinR = 4;
const int trigPinRL = 13;
const int echoPinRL = 12;

const int ledpin = 5;
const int buttonpin = 3;
int state =0; //버튼 상태
int flag = 1;

void setup() {
  Serial.begin(115200);
  DebugSerial.begin(115200);
  //초음파
  pinMode(trigPinR, OUTPUT);
  pinMode(echoPinR, INPUT);
  pinMode(trigPinRL, OUTPUT);
  pinMode(echoPinRL, INPUT);

  //LED
  pinMode(ledpin,OUTPUT);

  //BUTTION
  pinMode(buttonpin,INPUT);
  
  while(Serial.read()>= 0) {}
  while(!Serial);

//  /* SNIPE LoRa Initialization */
  if (!SNIPE.lora_init()) {
    DebugSerial.println("SNIPE LoRa Initialization Fail!");
    while (1);
  } 
  if (!SNIPE.lora_setAppKey(lora_app_key)) DebugSerial.println("SNIPE LoRa app key value has not been changed");
  if (!SNIPE.lora_setFreq(LORA_CH_1))DebugSerial.println("SNIPE LoRa Frequency value has not been changed");
  if (!SNIPE.lora_setSf(LORA_SF_7)) DebugSerial.println("SNIPE LoRa Sf value has not been changed");
  if (!SNIPE.lora_setRxtout(5000)) DebugSerial.println("SNIPE LoRa Rx Timout value has not been changed");
  
  DebugSerial.println("SNIPE LoRa Box start!");
}


void loop() {
  //측정
    int flag = 1;
    digitalWrite(trigPinR, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPinR, LOW);

    long distanceR = pulseIn(echoPinR, HIGH) / 58;

    digitalWrite(trigPinRL, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPinRL, LOW);

    long distanceL = pulseIn(echoPinRL, HIGH) / 58;

    Serial.print(distanceR);
    Serial.println( "CM");
    Serial.print(distanceL);
    Serial.println( "CM");

    //led+버튼 측정 : 버튼눌리면 빨간불 켜지고 ack 받으면 자동 꺼짐
    int value = digitalRead(buttonpin);
    
    //물건들어오면 값을 보냄
    if(distanceL < 7 || distanceR < 7 )
    {
      if(value == HIGH)
      {
        digitalWrite(ledpin,HIGH);
        flag = 0;
      }
    }

    //전송
    if(flag == 0)
    {
        char a[100];
        sprintf(a,"#B L:%ld,R:%ld",distanceL,distanceR);
        String full=a;
        if (SNIPE.lora_send(full))
        {
          DebugSerial.println("send success");
          DebugSerial.println(full);
//          DebugSerial.print(distanceR);
//          DebugSerial.println( "CM");
//          DebugSerial.print(distanceL);
//          DebugSerial.println( "CM");
          delay(100);

          //ACK받으면 
          String ver = SNIPE.lora_recv();
          if (ver == "#B ACK")
          {
            DebugSerial.println("recv success");
            flag = 1;
             //led 꺼짐
            digitalWrite(ledpin,LOW);        
          }
          else
          {
            DebugSerial.println("recv fail");
            delay(500);
          }
        }
       delay(1000);
     }
}
