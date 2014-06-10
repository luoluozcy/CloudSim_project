/****************************************************************************/
/*                  Copyright 2001, Trustees of Boston University.          */
/*                               All Rights Reserved.                       */
/*                                                                          */
/* Permission to use, copy, or modify this software and its documentation   */
/* for educational and research purposes only and without fee is hereby     */
/* granted, provided that this copyright notice appear on all copies and    */
/* supporting documentation.  For any other uses of this software, in       */
/* original or modified form, including but not limited to distribution in  */
/* whole or in part, specific prior permission must be obtained from Boston */
/* University.  These programs shall not be used, rewritten, or adapted as  */
/* the basis of a commercial software or hardware product without first     */
/* obtaining appropriate licenses from Boston University.  Boston University*/
/* and the author(s) make no representations about the suitability of this  */
/* software for any purpose.  It is provided "as is" without express or     */
/* implied warranty.                                                        */
/*                                                                          */
/****************************************************************************/
/*                                                                          */
/*  Author:     Alberto Medina                                              */
/*              Anukool Lakhina                                             */
/*  Title:     BRITE: Boston university Representative Topology gEnerator   */
/*  Revision:  2.0         4/02/2001                                        */
/****************************************************************************/

package org.cloudbus.cloudsim.brite.Model;

import org.cloudbus.cloudsim.brite.Graph.*;
import org.cloudbus.cloudsim.brite.Util.*;

import java.util.HashSet;

/** 
    All Topology generation in BRITE is governed by a Model Class.  A
    Model is a member variable of every topology and knows how to
    generate a representative graph.  <br><br> Class Model is the base
    abstract class from which all models in BRITE must be derived.
    For Router level models, consult the class {@link RouterModel
    RouterModel} and for models at the AS level, see the {@link
    ASModel ASModel} class */

public abstract class Model { 
  
    /**  
	The  NodePlacement method used when placing nodes on
	the plane.  Currently, we support RANDOM and HEAVY TAILED
	placement of nodes.  (values can be found in
	ModelConstants) */
    protected int nodePlacement;   


    /**  
	 The growth method used when building the graph.  Do we grow
	 incrementally or do we just place all the nodes at once and
	 then create the edges. (i.e.  GT_INCREMENTAL and GT_ALL
	 respectively - values found in ModelConstants).  More growth
	 models can be added.  */
    protected int growthType;
  
    /**  
	 A notion of preferential connectivity.  Not implemented in
	 any models included in this distribution.*/
    protected int prefConn;
    /** 
	Modeling Connection Locality.  Not implemented in any models
	included in this distribution*/
     protected int connLocality;

    /** 
	HS is the length of the plane (or number of unit size squares)*/
     protected int HS;

    /** 
	LS is the length of the inner squares in each HS square.  */
     protected int LS;

    /** m is the number of nodes each new node connects to*/
     protected int m;

    /** N is the number of nodes in the topology generated by this model */
     protected int N;
      
    /** Each model has one random number generator manager.  This
        RandomGenManager is usually created outside the Model and if specific
        seeds need to be used, it must be initialized at some point
        before generation via the setRandomGenManager() method. 
	Also please see Util.RandomGenManager*/
     protected RandomGenManager rm;

    /** This is a static Hashset that tracks node collisions when placing nodes in the plane.  It
        stores as keys a unique encoding of  node (x,y) coordinates (found in Util.Util.Encode)
	and as values a true/false indicating if this coordinate is occupied by a node already.
	As such, verifying if a node is about to be collided with another node is an O(1) operation
    */
     protected  static HashSet nodePositions = new	HashSet();


 
    /**  Set the RandomGenManager for this Model.  Usually this is set
	 by the creator of a Model, once the Model is successfully
	 created.  Each Model requires several independent random
	 number streams - eg one for placing the nodes, one for
	 connecting the nodes, one for assigning bandwidths etc.  The
	 RandomGenManager (found at Util.RandomGenManager) manages all
	 these individual random number generators and their
	 respective seeds.  Each Model thus has one RandomGenManager,
	 which is specific to this model only.
	 
	 @param rm The one RandomGenManager which manages the seeds
	 and random number generators for this model.  
	 
	 @see Util.RandomGenManager 
    */
  public void setRandomGenManager(RandomGenManager rm) {
    this.rm = rm;
  }
  

  /** Get method used by a Model instance.  Your sub-class may extend this.
      @return int*/
    public int getNodePlacement() { return nodePlacement; }

  /** Get method used by a Model instance.  Your sub-class may extend this.
      @return int*/
    public int getGrowthType() { return growthType; }

  
  public int getPrefModel() { return prefConn; }
  public int getConnLocal() { return connLocality; }

    /** Get the number of nodes in a topology to be generated by a Model instance.
      @return int*/
  public int getN() { return N;}

  /** Get the m parameter used by a Model instance.
      @return int*/
  public int getM() { return m; }
  

    //abstract public Model() { }  No constructor because simple "Model" is meaningless
    
  
  /**
     All Models that are derived from this base Model class, must
     override the Generate() method.  The Generate() method handles
     all the model-specific intricacies of: 1) Placing the Nodes 2)
     Interconnecting the Nodes 3) Assigning Edge weights (bandwidth,
     delay etc) And other model-specific functionality.
     
     @return Graph
  */
    abstract public Graph Generate();   /*subclasses worry with this*/
    
  /**
     All models have a string description which is returned by their
     toString() method.  Usually this is included when exporting the
     topology generated by that model to a file.
  
     @return String
  */
  abstract public String toString();
  
    
    
}









