package silhouette.samples;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import silhouette.shapes.*;
import silhouette.interactors.*;
import silhouette.constraints.*;

/**
 * This sample shows how to create editable text objects and how to
 * use constraints to align objects.
 */
public class EditableTextFrame extends JFrame {

  private CanvasShape canvas;

  EditableTextShape textbox;
  EditableTextShape textbox1;

    RectangleShape rs, rs1;

  public EditableTextFrame() {
    canvas = new CanvasShape();

    getContentPane().add (canvas.getSilPanel());
    this.setTitle("EditableTextFrame");

    // create a blue colored text string that is set in a white box
    textbox = new EditableTextShape(20, 20, "David");
    textbox.setFillColor(Color.white);
    textbox.setFilled(true);
    textbox.setLineColor(Color.blue);
    canvas.add(textbox);

    // create a rectangle that is positioned 40 pixels after the textbox
    rs = new RectangleShape(80, 20, 10, 10);
    rs.addConstraint(new silhouette.constraints.alignAfter(textbox, 40));
    canvas.add(rs);


    textbox1 = new EditableTextShape(20, 100, "Paul");
    textbox1.setFillColor(Color.white);
    textbox1.setFilled(true);
    textbox1.setLineColor(Color.blue);
    canvas.add(textbox1);

    rs1 = new RectangleShape(80, 100, 10, 10);
    //rs.addConstraint(new silhouette.constraints.alignAfter(textbox1, 40));
    canvas.add(rs1);


    constraint cn = new constraint () {

        public void formula(constrainedObject owner) {

	    RectangleShape self = (RectangleShape)owner;
	    CompositeShape parent = (CompositeShape)self.getParent();

	    if(textbox1.getWidth() > 70) {

	        self.setLeft( textbox1.getRight() + 30 );

	    }

	    else
		self.setLeft(0);


	}

    };

    rs1.addConstraint(cn);

    // create arrow line that connects the "Brad" textbox to rectangle "rs"
    SilArrowLine sl = new SilArrowLine(0, 0, 20, 20);
    sl.addConstraint(new silhouette.constraints.attachX1ToRight(textbox));
    sl.addConstraint(new silhouette.constraints.attachY1ToCenter(textbox));
    sl.addConstraint(new silhouette.constraints.attachX2ToLeft(rs));
    sl.addConstraint(new silhouette.constraints.attachY2ToCenter(rs));
    canvas.add(sl);

    // create a normal colored text string with a 48 point font
    Font newFont = textbox.getFont().deriveFont(48.0f);
    EditableTextShape nextbox = new EditableTextShape(20, 80, "Brad");
    nextbox.setFont(newFont);
    nextbox.addConstraint(new alignUnder(textbox, 30));
    //    nextbox.addConstraint(new silhouette.constraints.alignCenterX(canvas));
    nextbox.addConstraint(new constraint () {
      public void formula(constrainedObject owner) {
	  EditableTextShape self = (EditableTextShape)owner;
	  if (textbox1.getWidth() > textbox.getWidth()) 
	      self.setLeft(rs1.getLeft() + rs1.getWidth() + 20);
	  else
	      self.setLeft(rs.getLeft() + rs.getWidth());
      }
	});

    canvas.add(nextbox);

    // create a labeled rectangle
    RectangleShape rect = new RectangleShape(0, 0, 10, 10);
    CompositeShape labeledRect = new CompositeShape();
    labeledRect.addConstraint(new alignUnder(nextbox, 30));
    labeledRect.addConstraint(new alignLeft(nextbox));
    nextbox = new EditableTextShape(0, 10, "Brad");

    // put some constraints on the heights of the rectangles
    labeledRect.addConstraint(new silhouette.constraints.sameHeight(nextbox, 20));
    rect.addConstraint(new silhouette.constraints.sameHeight(labeledRect));

   labeledRect.add(rect, "frame");
   labeledRect.add(nextbox, "label");

   // horizontally center the label object within its parent. We could
   // use the pre-defined centerX constraint but we've written out the
   // constraint to show how a constraint can be defined.
   cn = new constraint () {
      public void formula(constrainedObject owner) {
	      EditableTextShape self = (EditableTextShape)owner;
        CompositeShape parent = (CompositeShape)self.getParent();
        self.setLeft(parent.getWidth()/2 - self.getWidth()/2);
      }};
   nextbox.addConstraint(cn);

   // make the width of labeledRect be 20 pixels wider than the label, but
   // ensure that the width of labeledRect is always at least 100 pixels.
    labeledRect.addConstraint(new constraint () {
      public void formula(constrainedObject owner) {
        CompositeShape self = (CompositeShape)owner;
        EditableTextShape label = (EditableTextShape)self.getShape("label");
        self.setWidth(Math.max(100, label.getWidth() + 20));
      }});

    // make rect have the same width as its parent
    rect.addConstraint(new constraint () {
      public void formula(constrainedObject owner) {
        RectangleShape self = (RectangleShape)owner;
        CompositeShape parent = (CompositeShape)self.getParent();
        self.setWidth(parent.getWidth());
      }});
    canvas.add(labeledRect);

    addWindowListener(new WindowAdapter()  {
      public void windowClosing(WindowEvent event) {
	dispose();
	System.exit(0);
      }
    } );
  }

  public static void main(String argv[]) {

    EditableTextFrame mywindow = new EditableTextFrame();

    mywindow.pack();
    mywindow.show();
  }

}
