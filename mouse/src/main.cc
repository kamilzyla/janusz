/*
 * main.cc
 *
 *  Created on: Nov 27, 2016
 *      Author: zak
 */

#include <Arduino.h>


int pwmMotorA=6;
int InMotorA1=8;
int InMotorA2=7;

void setup() {

  //Wyjście PWM kanału A
  pinMode(pwmMotorA, OUTPUT);

  //Wyjście cyfrowe AIN1
  pinMode(InMotorA1, OUTPUT);
  //Wyjście cyfrowe AIN2
  pinMode(InMotorA2, OUTPUT);

}

// Nieskonczona pętla
void loop() {

  //Kanał A

  //Ustawienie kierunku obrotów
  digitalWrite(InMotorA1, LOW);
  digitalWrite(InMotorA2, HIGH);

  //Ustawienie prędkości obrotowej na 50% (zakres PWM: 8bitów czyli 0-255)
  analogWrite(pwmMotorA, 255);

  //Opóźnienie 2s
  delay(2000);

  //Zmiana kierunku obrotów
//  digitalWrite(InMotorA1, HIGH);
//  digitalWrite(InMotorA2, LOW);

  //Opóźnienie 2s
//  delay(2000);

  //Zatrzymanie silnika - poprzez ustawianie wspołczynnika wypelnienia PWM na wartość 0
//  analogWrite(pwmMotorA,0);

  //Opóźnienie 2s
//  delay(2000);

}
