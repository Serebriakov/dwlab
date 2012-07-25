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
bbdoc: Group of sprites.
about: It has a lot of methods duplicating methods of TList.
End Rem
Type LTSpriteGroup Extends LTSprite
	Global SpriteShape:LTSprite = new LTSprite

	Rem
	bbdoc: List of sprites.
	End Rem
	Field Children:TList = New TList
	
	
	
	Method GetClassTitle:String()
		Return "Sprite group"
	End Method
	
	
	
	Method InsertSprite( Sprite:LTSprite )
		Sprite.X = ( Sprite.X - X ) / Width
		Sprite.Y = ( Sprite.Y - Y ) / Height
		Sprite.Width :/ Width
		Sprite.Height :/ Height
		Children.AddLast( Sprite )
	EndMethod
	
	
	
	Method RemoveSprite( Sprite:LTSprite )
		Local Link:TLink = Children.FindLink( Sprite )
		
		?debug
		If Not Link Then L_Error( "Removing sprite is not found in the group" )
		?
		
		Sprite.X = Sprite.X * Width + X
		Sprite.Y = Sprite.Y * Height + Y
		Sprite.Width :* Width
		Sprite.Height :* Height
		
		Link.Remove()
	EndMethod

	' ==================== Drawing ===================
	
	Method Draw()
		DrawGroup( Null, Self )
	End Method
	
	
	
	Method DrawUsingVisualizer( Vis:LTVisualizer )
		DrawGroup( Vis, Self )
	End Method
	
	
	
	Method DrawGroup( Vis:LTVisualizer, ParentShape:LTSprite )
		For Local Sprite:LTSprite = Eachin Children
			SetShape( SpriteShape, Sprite, ParentShape )
			Local ChildSpriteGroup:LTSpriteGroup = LTSpriteGroup( Sprite )
			If ChildSpriteGroup Then
				Local NewParentShape:LTSprite = New LTSprite
				SetShape( NewParentShape, ChildSpriteGroup, ParentShape )
				ChildSpriteGroup.DrawGroup( Vis, NewParentShape )
			ElseIf Vis = Null Then
				Sprite.Visualizer.DrawUsingSprite( Sprite, SpriteShape )
			Else
				Vis.DrawUsingSprite( Sprite, SpriteShape )
			End If
		Next
	End Method
	
	
	
	Method SetShape( Shape:LTSprite, Sprite:LTSprite, ParentShape:LTSprite )
		If Angle = 0 Then
			Shape.X = Sprite.X * ParentShape.Width + ParentShape.X
			Shape.Y = Sprite.Y * ParentShape.Height + ParentShape.Y
		Else
			Local RelativeX:Double = Sprite.X * ParentShape.Width
			Local RelativeY:Double = Sprite.Y * ParentShape.Height
			Shape.X = RelativeX * Cos( Angle ) - RelativeY * Sin( Angle ) + ParentShape.X
			Shape.Y = RelativeX * Sin( Angle ) + RelativeY * Cos( Angle ) + ParentShape.Y
		End If
		Shape.Width = ParentShape.Width * Sprite.Width
		Shape.Height = ParentShape.Height * Sprite.Height
		Shape.Angle = ParentShape.Angle + Sprite.Angle
	End Method
		
	' ==================== List wrapping methods ====================

	Method AddFirst:TLink( Sprite:LTShape )
		Return Children.AddFirst( Sprite )
	End Method
	
	
	
	
	Method AddLast:TLink( Sprite:LTSprite )
		Return Children.AddLast( Sprite )
	End Method
	
	
	
	Method Clear()
		Children.Clear()
	End Method
	
	
	
	Method Remove( Shape:LTShape )
		Children.Remove( Shape )
	End Method
	
	
	
	Method ValueAtIndex:LTShape( Index:Int )
		Return LTSprite( Children.ValueAtIndex( Index ) )
	End Method
	
	
	
	Method ObjectEnumerator:TListEnum()
		Return Children.ObjectEnumerator()
	End Method
	
	' ==================== Cloning ====================
	
	Method Clone:LTShape()
		Local NewSpriteGroup:LTSpriteGroup = New LTSpriteGroup
		For Local Shape:LTShape = Eachin NewSpriteGroup.Children
			NewSpriteGroup.Children.AddLast( Shape.Clone() )
		Next
		Return NewSpriteGroup
	End Method
	
	' ==================== Saving / loading ====================
	
	Method XMLIO( XMLObject:LTXMLObject )
		Super.XMLIO( XMLObject )
		XMLObject.ManageChildList( Children )
	End Method
End Type