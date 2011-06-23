'
' Graph usage demo - Digital Wizard's Lab example
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

SuperStrict

?win32
Framework brl.d3d7max2d
Import brl.directsoundaudio
?linux
Framework brl.glmax2d
Import brl.freeaudioaudio
?macos
Framework brl.glmax2d
Import brl.freeaudioaudio
?

Import brl.jpgloader
Import brl.pngloader
Import brl.oggloader

Import dwlab.frmwork

Include "TEditor.bmx"
Include "TMovePivot.bmx"
Include "TMakeLine.bmx"
Include "TGame.bmx"
Include "TGameMap.bmx"
Include "TGlobalMapEvent.bmx"

Global Editor:TEditor = New TEditor
'Game.Execute()
Editor.Execute()
