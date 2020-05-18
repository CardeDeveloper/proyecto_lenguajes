public  class Transition{
    private int from;
    private int to;
    private char character;

    public Transition(int from, int to, char character){
        this.from = from;
        this.to = to;
        this.character = character;
    }

    public int getFrom(){
        return this.from;
    }

    public void setFrom(int from){
        this.from = from;
    }

    public void setTo(int to){
        this.to = to;
    }
    public int getTo(){
        return this.to;
    }

    public char getCharacter(){
        return this.character;
    }

    public String toString(){
        return this.from + " -[" + this.character + "]-> "+this.to;
    }
    
    @Override
    public boolean equals(Object t1) {
    	if(!(t1 instanceof Transition)) return false;
    	Transition t2 = (Transition) t1;
    	return this.from == t2.from && this.to == t2.to && this.character == t2.character;
    }
}