
public class State {
    boolean isInitial = false;
    boolean isFinal = false;
    int id;
   
    public State(int index){
        this.id = index;
    }

    public int obtenerId(){
        return this.id;
    }

    public void setId(int newId){
        this.id = newId;
    }
    
    public String toString(){
        return "S: " + this.id + " (initial: " + isInitial + ", final:" + isFinal + ")";
    }

    public void setInitial(boolean init){
        this.isInitial = init;
    }

    public void setFinal(boolean isFinal){
        this.isFinal = isFinal;
    }

    public boolean getInitial(){
        return this.isInitial;
    }

    public boolean getFinal(){
        return this.isFinal;
    }
    
    @Override
    public boolean equals(Object t1) {
    	if(!(t1 instanceof State)) return false;
    	State t2 = (State) t1;
    	return this.id == t2.id;
    }
}