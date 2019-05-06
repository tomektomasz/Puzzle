package puzzle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.Math;

public class SlidingPuzzle {
	public static int n=3 ;
    public byte[][] board ;
    Point point;
    private String moves;

    public SlidingPuzzle() {		//konstruktor
        board =new byte[n][n];
        point=new Point(0,0);
        byte k=0;
        for(int i=0; i<n; i++) {
            for(int j=0;j<n;j++) {
                board[i][j]=k;
                k++;
            }
        }
    }
    public SlidingPuzzle(SlidingPuzzle parent) {		//konstruktor kopiujacy
        board =new byte[n][n];
        point = new Point(parent.point);
        for(int i=0; i<n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = parent.board[i][j];
            }
        }

        //point = (Point) parent.point.clone();

    }

    public String getMoves(){
        return this.moves;
    }

    public String toString() {		//wyswietlenie puzzli
        String txt="";
        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                txt+=(board[i][j]+" ");
            }
            txt+="\n";
        }
        return txt;
    }
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof SlidingPuzzle)) return false; 
        else{
            boolean the_same = true;
            SlidingPuzzle other = (SlidingPuzzle) obj;
            for(int i = 0; i < this.n; i++){
                Point p1 = this.showPlace(i);
                Point p2 = other.showPlace(i);
             
                if(p1.x != p2.x || p1.y != p2.y){
                    the_same = false;
                    break;
                }

            }
            return the_same;
        }
    }

    public Point showPlace(int nr) {		//pokazanie obecnego miejsca dla nr-u
        int x=0;
        int y=0;
        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                if(board[i][j]==nr) {
                    x=i;
                    y=j;
                }
            }
        }
        return new Point(x,y);
    }
    public boolean makeMove(char goal) {		//przesuniecie kafelka
        switch(goal) {
            case 'L':
                if((point.y)==0) return false;
                board[point.x][point.y]=board[point.x][point.y-1];
                board[point.x][point.y-1]=0;
                point.y=point.y-1;
                return true;
            case 'R':
                if((point.y+1)==n) return false;
                board[point.x][point.y]=board[point.x][point.y+1];
                board[point.x][point.y+1]=0;
                point.y=point.y+1;
                return true;
            case 'U':
                if((point.x)==0) return false;
                board[point.x][point.y]=board[point.x-1][point.y];
                board[point.x-1][point.y]=0;
                point.x=point.x-1;
                return true;
            case 'D':
                if((point.x+1)==n) return false;
                board[point.x][point.y]=board[point.x+1][point.y];
                board[point.x+1][point.y]=0;
                point.x=point.x+1;
                return true;
        }

        return false;
    }
    public void mixPuzzle(int many) {		//pomieszanie kafelkow
        Random fate;
        this.moves="";
        int a;
        fate = new Random();
        while(many > 0){
            a = fate.nextInt(4);
            char dir = "LRUD".charAt(a);
            if(makeMove(dir)){
            	this.moves += dir;
                many--;
            }
        }

    }
    public int hMissTiles() {		//funkcja heurystyczna misstilles
        int k=0;
        int sum=0;
        for(int i=0; i<n; i++) {
            for(int j=0;j<n;j++) {
                if(board[i][j]!=0) {
                    if(k!=board[i][j]) sum++;
                }
                k++;
            }
        }
        return sum;
    }
    public int hManhatan() {		//funkcja heurystyczna manhatan
        int k=0;
        int sum=0;
        for(int i=0; i<n; i++) {
            for(int j=0;j<n;j++) {
                if(board[i][j]!=0) {
                    sum+=Math.abs(i-showPlace(k).x);
                    sum+=Math.abs(j-showPlace(k).y);
                }
                k++;
            }
        }
        return sum;
    }

    private SlidingPuzzle getLowestScore(List<SlidingPuzzle> open_set, Map<String, Integer> score){
        if(open_set.size() == 0) return this;
        else{
            SlidingPuzzle nearest = open_set.get(0);
            for(SlidingPuzzle puzzle: open_set){
                if(score.get(puzzle.toString()) < score.get(nearest.toString())) {
                    nearest = puzzle;
                }
            }
            return nearest;
        }
    }

    private List<SlidingPuzzle> generateChildren(){
        List<SlidingPuzzle> children = new ArrayList<>();

        if((point.y)!=0){
            SlidingPuzzle p = new SlidingPuzzle(this);
            p.makeMove('L');
            children.add(p);
        }

        if((point.y+1)!=n){
            SlidingPuzzle p = new SlidingPuzzle(this);
            p.makeMove('R');
            children.add(p);
        }

        if((point.x+1)!=n){
            SlidingPuzzle p = new SlidingPuzzle(this);
            p.makeMove('D');
            children.add(p);
        }

        if((point.x)!=0){
            SlidingPuzzle p = new SlidingPuzzle(this);
            p.makeMove('U');
            children.add(p);
        }

        return children;
    }

    private String getDirection(SlidingPuzzle first, SlidingPuzzle next){
     
        SlidingPuzzle temp_l = new SlidingPuzzle(first);
        temp_l.makeMove('L');
        if (temp_l.equals(next)) return "L";

        SlidingPuzzle temp_r = new SlidingPuzzle(first);
        temp_r.makeMove('R');
        if (temp_r.equals(next)) return "R";

        SlidingPuzzle temp_t = new SlidingPuzzle(first);
        temp_t.makeMove('U');
        if (temp_t.equals(next)) return "U";

        SlidingPuzzle temp_b = new SlidingPuzzle(first);
        temp_b.makeMove('D');
        if (temp_b.equals(next)) return "D";

        return "X";
    }

 
    private Integer estimate(SlidingPuzzle puzzle, String type){
        if(type.equals("misplaced")){
            return puzzle.hMissTiles();
        } else if (type.equals("manhattan")){
            return puzzle.hManhatan();
        } else {
            return puzzle.hManhatan();
        }
    }

    private List<String> reconstruct_way(Map<String, List<String> > way, String current) {
        List<String> moves = new ArrayList<>();
        while (way.keySet().contains(current)) {
            moves.add(way.get(current).get(1)); 
            current = way.get(current).get(0); 
        }

        Collections.reverse(moves);
        String directions = "";

      
        for(int i = 0; i < moves.size(); i++){
            directions += moves.get(i);
            this.makeMove(moves.get(i).charAt(0));
        }

        
        this.moves = "";

      
        List<String> result = new ArrayList<>();
        result.add(directions);
        result.add(String.valueOf(way.keySet().size()));

        return result;
    }

    public Boolean is_in_list(List<SlidingPuzzle> list){
        for(int i = 0; i < list.size(); i++){
            if(this.toString().equals(list.get(i).toString())){
                return true;
            }
        }
     
        return false;
    }
 
    public List<String> solve(){
        return this.solve("manhattan");
    }

    public List<String> solve(String type) {
        SlidingPuzzle goal = new SlidingPuzzle();

        List<SlidingPuzzle> closed_set = new ArrayList<>();
        List<SlidingPuzzle> open_set = new ArrayList<>();

        Map<String, List<String>> way = new HashMap<>();

        Map<String, Integer> g_score = new HashMap<>();
        Map<String, Integer> f_score = new HashMap<>();

        open_set.add(this);
        g_score.put(this.toString(), 0);
        f_score.put(this.toString(), Integer.MAX_VALUE);

        SlidingPuzzle current;
        while (open_set.size() > 0) {
            current = getLowestScore(open_set, f_score);

            if (current.equals(goal)) return reconstruct_way(way, current.toString());

          
            open_set.remove(current);
            closed_set.add(current);


            for (SlidingPuzzle neighbor : current.generateChildren()) { 
                if (!neighbor.is_in_list(closed_set)) {
                    Integer neighbor_score = g_score.get(current.toString()) + 1;

                    if (!neighbor.is_in_list(open_set)) {
                        open_set.add(neighbor);
                        g_score.put(neighbor.toString(), Integer.MAX_VALUE);
                        f_score.put(neighbor.toString(), Integer.MAX_VALUE);
                    }

                    if (neighbor_score < g_score.get(neighbor.toString())) {
                        List<String> info = new ArrayList<String>();
                        info.add(current.toString());
                        info.add(getDirection(current, neighbor));

                        way.put(neighbor.toString(), info);
                        g_score.put(neighbor.toString(), neighbor_score);
                        f_score.put(neighbor.toString(), neighbor_score + estimate(neighbor, type));
                    }
                }
            }

        }

        return Arrays.asList("Brak", "");
    }
    
    public static void main(String[] args) {
        SlidingPuzzle sp= new SlidingPuzzle();

        System.out.println(sp.toString());
       

        System.out.println("MIESZANIE I ROZWIĄZYWANIE\n");
      
        sp.solve();

        sp.mixPuzzle(100);
        System.out.println("Zrobione losowe ruchy:\n" + sp.getMoves() );
        System.out.println("Otrzymana plansza:");
        System.out.println(sp.toString());       

        List<String> solution;
        
        SlidingPuzzle sp_misplaced = new SlidingPuzzle(sp);
        SlidingPuzzle sp_manhattan = new SlidingPuzzle(sp);

        System.out.println("Misplaced:");
        solution = sp_misplaced.solve("misplaced");

        System.out.println("Kierunki " + solution.get(0));
        System.out.println("Ilość stanow: " + solution.get(1));

        System.out.println(sp_misplaced.toString());

        System.out.println("Manhattan:");
        solution = sp_manhattan.solve("manhattan");

        System.out.println("Kierunki " + solution.get(0));
        System.out.println("Ilość stanow: " + solution.get(1));

        System.out.println(sp_manhattan.toString());

    }

}
