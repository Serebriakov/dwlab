'
' Super Mario Bros - Digital Wizard's Lab example
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Include "TMushroom.bmx"
Include "TOneUpMushroom.bmx"
Include "TFireFlower.bmx"
Include "TStarMan.bmx"

Type TBonus Extends LTVectorSprite
	Const Impulse:Double = 8.0
	
	Method Collect()
	End Method
End Type