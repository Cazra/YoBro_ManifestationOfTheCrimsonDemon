Yo Bro: Manifestation of the Crimson Demon 
README

=============
License:

Copyright (c) 2011, Stephen Lindberg (Neonair Games)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yo Bro, Neonair Games, nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

===============
Preface:

Yo Bro: Manifestation of the Crimson Demon is a Java game project I developed
as my capstone project for my BS of Computer Science degree at Tennessee 
Technological University. Over the course of the semester, I developed my own
Java game engine, using the Java AWT graphics and swing events frameworks. Using
my game engine, I completed the first stage of the game, the tutorial stage, 
and the game's main menu. 

In the couple months after presenting my project in
front of my peers and mentors in the Computer Science department (and acing it!)
I continued on to implement Mode 7 backgrounds and 3 more playable stages. After
that, I got hired as a junior software engineer at Oak Ridge National Lab, 
had less time to work on the game. There I learned better coding practices and 
developed a more advanced and more well-engineered organized Java 2D game 
engine, Pwnee2D (also on my github page). 

Yo Bro's engine quickly became deprecated in contrast to the point where
I'd prefer to just completely start over on the project with my new engine 
(Pwnee2D, also on my github) instead of continuing with the old game engine. 
So, I am officially discontinuing this version of Yo Bro, and releasing it
publicly as open source. I also decided to release the unfinished game to
impress potential employers.

====================
Requirements: 
Java JRE 7 or higher
Apache Ant (only for compiling)
JAVA_HOME environment variable must be set (only for compiling)

====================
Running the game:

There are two ways to run the game. Neither method requires you to compile it
from the source, as I have already compiled an executable jar file for your 
convenience. I developed and tested the game on a Windows 7 laptop with 4 GB of
RAM and a factory video card. It runs without any problems for me, but results
may vary for different OS's and hardware.

Run run.bat (Windows)

 OR

Open a command line in the project's root directory and enter:
> java -jar latest/YoBro.jar [fullscreen]

If the fullscreen argument is given, the game will run in full-screen mode.
Otherwise it will run in windowed mode.

=====================
Compiling from source:

Open a command line in the project's root directory and enter:
> ant clean all

=====================
Terms of use:

You are free to play Yo Bro, peruse its source code and resources, and 
distribute it for free. 

If you plan to use any of Yo Bro's graphical resources 
or significant parts of the source code, please contact me at 
sllindberg21@students.tntech.edu to ask for permission.

======================
Closing remarks:

I hope that you all have fun playing what I have completed of Yo Bro. 
I certainly enjoyed working on it! Maybe one day I'll pick the project back up 
again to recreate it from scratch using Pwnee2D or whatever the latest iteration
of my Java game engine is.
