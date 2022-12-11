/*CentterLora: 1)상자로부터의 전송 값을 aws에 올림 2)상자 보안에게 명령*/
#include <SoftwareSerial.h>
#include "SNIPE.h"
#include <ESP8266WiFi.h>
#include <PubSubClient.h> // version 2.7.0
#include <time.h>
#include <ArduinoJson.h>
#include "credentials.h"

//[waring]wemos는 우노보드와 핀번호가 다름
#define TXpin 13
#define RXpin 15
#define LED 2
#define BTNOPEN 8
#define BTNCLOSE 9

SoftwareSerial DebugSerial(RXpin,TXpin);
SNIPE SNIPE(Serial);
//16byte hex key
String lora_app_key = "11 22 33 44 55 66 77 88 99 aa bb cc dd ee ff 00";  

/*변수*/
int flag=1;//send-0/recv-1
long lastMsg = 0;
char msg[50];
int value = 0;
char payload[512];
String v;
String s_sendData;

//json을 위한 설정
StaticJsonDocument<200> doc;
DeserializationError error;
JsonObject root;

/*aws 초기세팅*/
//const char *ssid = "KT_WLAN_E5B5";  // 와이파이 이름
//const char *pass = "0000004b51";      // 와이파이 비밀번호
const char *ssid = "ssyy";  // 와이파이 이름
const char *pass = "20010226";      // 와이파이 비밀번호
const char *thingId = "Wemos";          // 사물 이름 (thing ID) 
const char *host = "a2w8bfoxls5op4-ats.iot.ap-northeast-2.amazonaws.com";//"arn:aws:iot:ap-northeast-2:475939232926:thing/Wemos"; // AWS IoT Core 주소
const char* outTopic = "$aws/things/Wemos/shadow/update"; //aws 업로드하는 주제방식
const char* inTopic = "inTopic"; 

String sChipID; // mac address를 문자로 기기를 구분하는 기호로 사용
char cChipID[40];


//사이트 연결에 대한 callback함수
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();

  deserializeJson(doc,payload);
  root = doc.as<JsonObject>();
  int value = root["on"];
  Serial.println(value);
}
//사물인증서와 프라이빗키 host(사물엔드포인트)를 이용하여 연결 설정하는 부분
X509List ca(ca_str); 
X509List cert(cert_str);
PrivateKey key(key_str);
WiFiClientSecure wifiClient;
PubSubClient client(host, 8883,callback, wifiClient); //set  MQTT port number to 8883 as per //standard
//사이트와 연결 끊어지면 다시 연결
void reconnect() {
  Serial.println("(in reconnect)");
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(thingId)) {
      Serial.println("connected");
//      // Once connected, publish an announcement...
//      client.publish(outTopic, "hello world(reconnect)");
//      // ... and resubscribe
//      client.subscribe(inTopic); //무슨 의미?
    } else { //사이트와 연결에러시-5초후 다시 연결
      Serial.print("failed, rc=");
      Serial.print(client.state());
//      Serial.println(" try again in 5 seconds");
//
//      char buf[256];
//      wifiClient.getLastSSLError(buf,256);
//      Serial.print("WiFiClientSecure SSL error: ");
//      Serial.println(buf);
//
//      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

//타이밍 맞추는 코드
// Set time via NTP, as required for x.509 validation 
void setClock() {
  Serial.println("(in callback)");
  configTime(3 * 3600, 0, "pool.ntp.org", "time.nist.gov");

  Serial.print("Waiting for NTP time sync: ");
  time_t now = time(nullptr);
  while (now < 8 * 3600 * 2) {
    delay(500);
    Serial.print(".");
    now = time(nullptr);
  }
  Serial.println("");
  struct tm timeinfo;
  gmtime_r(&now, &timeinfo);
  Serial.print("Current time: ");
  Serial.print(asctime(&timeinfo));
}


void setup() {
  Serial.begin(115200);
  DebugSerial.begin(115200); //테라텀(로라)
  pinMode(LED, OUTPUT); 
  pinMode(BTNOPEN, INPUT);
  pinMode(BTNCLOSE, INPUT);
  Serial.setDebugOutput(true);
  Serial.println();
  
  //이름 자동으로 생성
  uint8_t chipid[6]="";
  WiFi.macAddress(chipid);
  sprintf(cChipID,"%02x%02x%02x%02x%02x%02x%c",chipid[5], chipid[4], chipid[3], chipid[2], chipid[1], chipid[0],0);
  sChipID=String(cChipID);
  thingId=cChipID;

  //와이파이 연결
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, pass);
  //연결될때까지 기다림
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");

  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  //
  wifiClient.setTrustAnchors(&ca);
  wifiClient.setClientRSACert(&cert, &key);
  Serial.println("");

  setClock();
  client.setServer(host, 8883);
  client.setCallback(callback);
  /*로라*/
  //시리얼 내용 초기화
  while(Serial.read()>= 0) {}
  while(!Serial);

  //로라 초기화. 연결될까지 while문
  if (!SNIPE.lora_init()) {
    DebugSerial.println("SNIPE LoRa Initialization Fail!");
    while (1);
  }
  if (!SNIPE.lora_setAppKey(lora_app_key)) DebugSerial.println("SNIPE LoRa app key value has not been changed");
  if (!SNIPE.lora_setFreq(LORA_CH_1))DebugSerial.println("SNIPE LoRa Frequency value has not been changed");
  if (!SNIPE.lora_setSf(LORA_SF_7)) DebugSerial.println("SNIPE LoRa Sf value has not been changed");
  if (!SNIPE.lora_setRxtout(5000)) DebugSerial.println("SNIPE LoRa Rx Timout value has not been changed");
  DebugSerial.println("SNIPE Center Wemos LoRa start!");
  
}

void loop() {
  btnCheck(BTNOPEN);
  btnCheck(BTNCLOSE);
  if(flag==1)//분실물 습득 신호를 받음
  {
    v = SNIPE.lora_recv();//"#B L:1,R:1";////
    delay(300); 
    //분실물 습득 신호 값이 맞을 때, AWS에 올리기
    Serial.println(v);
    Serial.println(v.indexOf("#B"));
    if(v.indexOf('#B')!=-1){ //#B가 있는지 확인, 없으면 -1
      digitalWrite(LED,HIGH); //부저나 led 온
      if(SNIPE.lora_send("#B ACK")){ 
        DebugSerial.println("#B ACK succes");
        if (!client.connected()) {
          reconnect();
        }
        client.loop();
        long now = millis();
        if (now - lastMsg > 5000) { 
          lastMsg = now;
          ++value;  //인덱스
          //받은 코드를 분해하는 부분
          v=v.substring(3);//실제 데이터 부분만 사용함
          String v_l=v.substring(2,v.indexOf(","));//index 2부터 ,전까지
          String v_r=v.substring(v.indexOf(",")+3,10);//끝을 10이라고 가정하고
          sprintf(payload,"{\"state\":{\"reported\":{\"left\":\"%s\",\"right\":\"%s\",\"finded\":\"false\"}}}",v_l,v_r);
          Serial.print("Publish message: ");
          Serial.println(payload);
          client.publish(outTopic, payload);//실제로 보내는 부분
          digitalWrite(LED,LOW);
          delay(100);
        }
        Serial.println("upload done");//
        digitalWrite(LED,LOW);
      }
    }else
    { //#B 데이터 오는지 안오는지 체크안해도 되면 이후 지워도 됨
      DebugSerial.println("No #B data");
    } 
  }
  else //상자 뚜껑 open-close
  { 
    if (SNIPE.lora_send(s_sendData))
        {
          DebugSerial.println("send success to camera");
          
          String ver = SNIPE.lora_recv();
          DebugSerial.println(ver);

          if (ver == "#C ACK") //카메라로 부터 ACK 받음
          {
            DebugSerial.println("#C ACK success");
            flag=1;      
          }
          else
          {
            DebugSerial.println("#C ACK fail");
            delay(500);
          }
        }
  }
}

void btnCheck(int b){
  int btn=digitalRead(b); //1 or 0
  Serial.print("btn(pin-");Serial.print(b);Serial.print(")");
  Serial.println(btn);
   if(btn&&b==BTNOPEN) //버튼 상태 체크
  {
    flag=2; //버튼 명령을 보안으로 
    s_sendData="#S OPEN";
  }else if(btn&&b==BTNCLOSE){
    flag=3;
    s_sendData="#S CLOSE";
  }
}
