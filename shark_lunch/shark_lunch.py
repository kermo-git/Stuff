#Kermo Saarse 2017
from pygame import *
from random import randint
from time import sleep

#Avab kõik pildifailid.
title = image.load('title.png')
instructions = image.load('instructions.png')
success = image.load('success.png')
wastedimg = image.load('wastedimg.png')
sharkimg = image.load('sharkimg.png')
fishimg = image.load('fishimg.png')
poisonimg = image.load('poisonimg.png')
greyfish = image.load('greyfish.png')
greypoison = image.load('greypoison.png')

#Klass, mille järgi luuakse haikala.
class Shark(object):
    
    def __init__(self, img, angle, xbool, x, y):
        self.img = img
        #angle on nurk, mille järgi hai pilti pööratakse.
        self.angle = angle
        #xbool on tõeväärtus, mis on määrab, kas hai on näoga paremale (True) või vasakule (False).
        self.xbool = xbool
        self.x = x
        self.y = y
    
    #Funktsioon, mis kuvab ekraanile kala pildi õige suunaga.
    def blit_img(self):
        rotate_img = transform.rotate(self.img, self.angle)
        screen.blit(transform.flip(rotate_img, self.xbool, False), (self.x, self.y))

    #Funktsioon, mis liigutab haid vastavalt tõeväärtustele, mis saadakse
    #nooleklahvide vajutamisel.
    def moveshark(self, up, down, left, right):
        if up:
            self.y -= 3
            self.angle = 315
        if down:
            self.y += 3
            self.angle = 45
        if left:
            self.x -= 3
            self.xbool = False
        if right:
            self.x += 3
            self.xbool = True
        if (left or right) and (not (up or down)):
            self.angle = 0
        if not (up or down or left or right):
            self.angle = 0
            if self.y <= 480:
                self.y += 1
        self.blit_img()

#Klass, mille järgi luuakse kalad.
class Fish(Shark):

    def __init__(self, img, grey, angle, xbool, a, x, y):
        self.img = img
        #grey on mustvalge versioon kala pildist.
        self.grey = grey
        self.angle = angle
        self.xbool = xbool
        #a on juhuslik number, mille abil genereeritakse kalale algasukoht.
        self.a = a
        self.x = x
        self.y = y

    #Funktsioon, mis annab kalale uue algasukoha ekraani paremas(a = 1) või vasakus(a = 0) servas.
    def change_location(self):
        k = randint(0,15)
        self.y = 40 + 35*k
        self.a = randint(0, 1)
        if self.a == 0:
            self.x = -84
            self.xbool = True
        if self.a == 1:
            self.x = 984
            self.xbool = False

    #Funktsioon, mis kala liigutab.        
    def movefish(self):
        if self.a == 0:
            self.x += 3
        if self.a == 1:
            self.x -= 3
        self.blit_img()

#Funktsioon, mis loob kala ja annab talle esimese algasukoha, kasutades klassi Fish.
def create_fish(img, grey):
    a = randint(0, 1)
    if a == 0:
        x = -84
        xbool = True
    if a == 1:
        x = 984
        xbool = False
    k = randint(0,15)
    y = 40 + 35*k
    fish = Fish(img, grey, 0, xbool, a, x, y)
    return fish

#Funktsioon, mis teeb kindlaks, kas hai on mõne kala ära söönud ja kas kala on ekraani teise serva jõudnud.
def gameplay(fish):
    global score
    #Võrdleb hai ja kala koordinaate
    fish_x = fish.x + 42
    fish_y = fish.y + 17
    if shark.angle == 0:
        shark_x = shark.x + 89
        shark_y = shark.y + 89
        case = (abs(shark_x - fish_x) <= 80) and (abs(shark_y - fish_y) <= 30)
    else:
        shark_x = shark.x + 125
        shark_y = shark.y + 125
        case = (abs(shark_x - fish_x) <= 60) and (abs(shark_y - fish_y) <= 60)
    if case:
        #Kui hai sööb ära mürgikala, siis tagastab True
        if fish in poisonlist:
            return True
        #Kui hai sööb ära tavalise kala, siis suurendab skoori ja
        #genereerib kalale uue asukoha.
        else:
            score +=1
            fish.change_location()
    #Kui kala on jõudnud ekraani teise serva ujuda, siis
    #genereerib kalale uue asukoha.
    if fish.x < -84 or fish.x > 984:
        fish.change_location()
    #Kala ujub edasi.
    fish.movefish()

#Funktsioon, mis kuvab ekraanile teksti.
def blit_text(font, text, color, x, y):
    message = font.render(text, True, color)
    screen.blit(message, (x, y))

#Funktsioon, mis kirjeldab tsüklit, kus ekraanil kuvatakse ainult ühte pilti.
def loop(img, text = False):
    screen.blit(img, (0, 0))
    if text:
        blit_text(font2, 'You ate ' + str(score) + ' fish!', (255, 255, 255), 210, 400)
    display.flip()
    done = False
    while not done:
        for thing in event.get():
            if thing.type == QUIT:
                quit()
            if thing.type == KEYDOWN and thing.key == K_RETURN:
                done = True
                break

#Käivitab Pygame'i, defineerib fondid, loob ekraani ja avab helifailid.
init()
font1 = font.SysFont('impact', 30)
font2 = font.SysFont('impact', 72)
screen = display.set_mode((900, 600))
display.set_caption('Shark Lunch')
jaws = mixer.Sound('jaws.ogg')
wastedsound = mixer.Sound('wastedsound.ogg')

#Kuvab tiitellehe.
loop(title)
        
while True:
    #Kuvab mängujuhised.
    loop(instructions)

    #Loob hai.
    shark = Shark(sharkimg, 0, True, 361, 211)
    #Loob 4 tavalist kala ja paigutab need ühte listi.
    fishlist = []
    for i in range(4):
        fishlist.append(create_fish(fishimg, greyfish))
    #Loob 3 mürgikala ja paigutab need teise listi.
    poisonlist = []
    for i in range(3):
        poisonlist.append(create_fish(poisonimg, greypoison))
    #Käivitab taimeri, nullib skoori, äratab hai ellu ja paneb muusika mängima.
    start = time.get_ticks()
    score = 0
    dead = False
    jaws.play()
    
    #Algab mängutsükkel.
    while not dead:
        clock = time.Clock()
        clock.tick(150)
        for thing in event.get():
            if thing.type == QUIT:
                quit()
        screen.fill((0, 130, 185))

        #For tsüklid käivad läbi mõlemad kalade listid ja iga kala korral
        #rakendavad funktsiooni gameplay.
        for fish in fishlist:
            gameplay(fish)
        for poison in poisonlist:
            if gameplay(poison):
                dead = True
                break
        
        #Teeb kindlaks, milliseid nooleklahve kasutaja vajutab ja loob vastavad tõeväärtused.
        #Tagab selle, et hai ei saaks ekraani piiridest välja ujuda.
        press = key.get_pressed()
        up = press[K_UP] and shark.y >= -70
        down = press[K_DOWN] and shark.y <= 480
        left = press[K_LEFT] and shark.x >= -20
        right = press[K_RIGHT] and shark.x <= 750
        #Liigutab haikala.
        shark.moveshark(up, down, left, right)

        #Kuvab ekraani üleval vasakus servas skoori.
        blit_text(font1, 'SCORE : ' + str(score), (0, 0, 0), 10, 10)
        
        #Mõõdab aega ja kuvab järelejäänud sekundid ekraani üleval paremas nurgas.
        seconds = (time.get_ticks() - start)/1000
        blit_text(font1, 'TIME : ' + str(60 - int(seconds)), (0, 0, 0), 785, 10)

        display.flip()
        
        #Kui 60 sekundit mängu algusest saab täis, lõpeb mängutsükkel ära.
        if seconds > 60:
            jaws.stop()
            break
        
    #Kui hai saab surma, muutub kogu pilt mustvalgeks ja mäng saab läbi.
    if dead:
        jaws.stop()
        screen.fill((97, 97, 97))
        #Kuvab hai pildi ja tekstid uuesti.
        shark.blit_img()
        blit_text(font1, 'TIME : ' + str(60 - int(seconds)), (0, 0, 0), 785, 10)
        blit_text(font1, 'SCORE : ' + str(score), (0, 0, 0), 10, 10)
        #Kuvab iga kala pildi asemele mustvalge pildi.
        for fish in fishlist:
            fish.img = fish.grey
            fish.blit_img()
        for poison in poisonlist:
            poison.img = poison.grey
            poison.blit_img()
        #Mängib surmaheli ja ootab 2.8 sekundit.
        wastedsound.play()
        display.flip()
        sleep(2.8)
        loop(wastedimg)
    #Kui hai oli mängu lõppedes elus, kuvab söödud
    #tavaliste kalade arvu.
    else:
        loop(success, True)
