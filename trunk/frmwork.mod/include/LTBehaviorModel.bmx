'
' Digital Wizard's Lab - game development framework
' Copyright (C) 2010, Matt Merkulov
'
' All rights reserved. Use of this code is allowed under the
' Artistic License 2.0 terms, as specified in the license.txt
' file distributed with this code, or available from
' http://www.opensource.org/licenses/artistic-license-2.0.php
'

Rem
bbdoc: Behavior model is the object which can be attached to the shape and affect its state.
returns: 
about: 
End Rem
Type LTBehaviorModel Extends LTObject
	Field Active:Int
	Field Link:TLink
	
	
	
	Rem
	bbdoc: Initialization method.
	about: It will be executed when model will be attached to shape.
	Fill it with model initialization commands. 
	End Rem
	Method Init( Shape:LTShape )
	End Method

	
	
	Rem
	bbdoc: Activation method.
	about: It will be executed when model will be activated (and when attached too if you didn't set activation flag to False).
	End Rem
	Method Activate( Shape:LTShape )
	End Method
	
	
	
	Rem
	bbdoc: Deactivation method.
	about: It will be executed when model will be activated (and when removed too if it was active).
	End Rem
	Method Deactivate( Shape:LTShape )
	End Method
	
	
	
	Rem
	bbdoc: Watching method.
	about: This method will be executed by shape default Act() method if the model will be inactive.
	Fill it with commands which will check certain conditions and activate model.
	End Rem
	Method Watch( Shape:LTShape )
	End Method
	
	
	
	Rem
	bbdoc: Model applying method.
	about: This method will be executed by shape default Act() method if the model will be active.
	Fill it with commands which are affect shape in the way of corresponding behavior.
	End Rem
	Method ApplyTo( Shape:LTShape )
	End Method
	
	
	
	Rem
	bbdoc: Collision with sprite handleing method.
	about: It will be executed by default HandleCollisionWithSprite method if this model will be active.
	Fill it with sprite collision reaction commands which need to be executed when the model is active.
	End Rem
	Method HandleCollisionWithSprite( Sprite1:LTSprite, Sprite2:LTSprite, CollisionType:Int )
	End Method

	
	
	Rem
	bbdoc: Collision with sprite handleing method.
	about: It will be executed by default HandleCollisionWithTile method if this model will be active.
	Fill it with tile collision reaction commands which need to be executed when the model is active.
	End Rem
	Method HandleCollisionWithTile( Sprite:LTSprite, TileMap:LTTileMap, TileX:Int, TileY:Int, CollisionType:Int )
	End Method

	
	
	Rem
	bbdoc: Activates behavior model.
	about: For use inside model's methods.
	End Rem
	Method ActivateModel( Shape:LTShape )
		Activate( Shape )
		Active = True
	End Method
	
	
	
	Rem
	bbdoc: Deactivates behavior model.
	about: For use inside model's methods.
	End Rem
	Method DeactivateModel( Shape:LTShape )
		Deactivate( Shape )
		Active = False
	End Method
	
	
	
	Rem
	bbdoc: Removes behavior model.
	about: Model will be deactivated before removal.
	End Rem
	Method Remove( Shape:LTShape )
		If Active Then DeactivateModel( Shape )
		Link.Remove()
	End Method
End Type