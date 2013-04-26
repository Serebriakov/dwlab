'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Module dwlab.music

Import brl.linkedlist

Rem
bbdoc: Global variable for operating music.
about: Use it to music 
End Rem
Global L_Music:LTMusicHandler = New LTMusicHandler

Rem
bbdoc: Music handler template.
End Rem
Type LTMusicHandler
	Global PauseMusicFadingPeriod:Int = 500
	Global NextMusicFadingPeriod:Int = 1000
	Global AllEntries:TList = New TList
	Global Entries:TList = New TList
	Global NextMus:Int
	Global Period:Double
	Global OperationStartTime:Int
	Global Volume:Double = 1.0
	Global ForceRepeat:Int
	
	Global MusicMode:Int = Stopped
	
	Const Normal:Int = 0
	Const Paused:Int = 1
	Const Fading:Int = 2
	Const Rising:Int = 3
	Const Stopped:Int = 4

	Rem
	bbdoc: Method for preloading music files.
	about: Preload music files before use.
	End Rem
	Method Preload( FileName:String, Name:String )
	End Method
	
	Rem
	bbdoc: Method for adding preloaded music to music sequence.
	about: Specify music name given on preload. You can make this music endlessly looped and specify its playing rate.
	End Rem
	Method Add( Name:String, Looped:Int = False, Rate:Double = 1.0 )
	End Method
	
	Rem
	bbdoc: Method for starting first music in the sequence.
	End Rem
	Method Start()
	End Method
	
	Rem
	bbdoc: Method for stopping music..
	End Rem
	Method StopMusic()
	End Method
	
	Rem
	bbdoc: Method for clearing music sequence.
	about: Use it before constructing new music sequence
	End Rem
	Method ClearMusic( FadeOut:Int = False )
		Entries.Clear()
		NextMusic( FadeOut )
	End Method
	
	Rem
	bbdoc: Method for pausing music.
	about: You can make current music volume to fade out before stopping.
	End Rem
	Method Pause( FadeOut:Int = False )
		If MusicMode = Rising Then
			Period = PauseMusicFadingPeriod + OperationStartTime - MilliSecs()
			OperationStartTime =  MilliSecs() * 2 - OperationStartTime
		ElseIf MusicMode = Normal Then
			OperationStartTime = MilliSecs()
			Period = PauseMusicFadingPeriod
		Else
			Return
		End If
		If Not FadeOut Then Period = 0
		MusicMode = Fading
		NextMus = False
		OperationStartTime = MilliSecs()
	End Method
	
	Rem
	bbdoc: Method for resumig paused music.
	about: You can make current music volume to fade in on start.
	End Rem
	Method Resume( FadeIn:Int = False )
		If MusicMode = Fading Then
			Period = PauseMusicFadingPeriod + OperationStartTime - MilliSecs()
			OperationStartTime =  MilliSecs() * 2 - OperationStartTime
		ElseIf MusicMode = Paused Then
			OperationStartTime = MilliSecs()
			Period = PauseMusicFadingPeriod
		Else
			Return
		End If
		If Not FadeIn Then Period = 0
		MusicMode = Rising
	End Method
	
	Rem
	bbdoc: Method for switching to next music in the sequence.
	about: You can make current music volume to fade out before switching to next one.
	End Rem
	Method NextMusic( FadeOut:Int = False, RemoveFirstEntry:Int = True )
		If Entries.IsEmpty() Then Return
		If FadeOut Then
			Period = NextMusicFadingPeriod
		Else
			Period = 0
		End If
		MusicMode = Fading
		If RemoveFirstEntry Then Entries.RemoveFirst()
		NextMus = True
		OperationStartTime = MilliSecs()
	End Method
	
	Rem
	bbdoc: Method for switching music playing.
	about: Stopped music will be started, paused music will be resumed and playing music will be paused.
	You can make current music volume to fade in on start.
	End Rem
	Method SwitchMusicPlaying( Fade:Int = False )
		Select MusicMode
			Case Normal, Rising
				Pause( Fade )
			Case Paused, Fading
				Resume( Fade )
			Case Stopped
				Start()
		End Select
	End Method
	
	Rem
	bbdoc: Method for managing music play.
	about: You should include execution of this method in main loop.
	End Rem
	Method Manage()
	End Method
	
	Method SetVolume( Vol:Double )
	End Method
End Type



Type LTMusicEntry
	Field Name:String
	Field Looped:Int
	Field Rate:Double
End Type