package puzzle;

import java.awt.Point;
import java.util.Random;
import java.lang.Math;

public class SlidingPuzzle {		
	public static int n=3 ;
	public byte[][] board ;
	Point point;
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
		point=new Point(0,0);
		for(int i=0; i<n; i++) {
			for(int j=0;j<n;j++) {
				board[i][j]=parent.board[i][j];
			}
		}
		point=parent.point;
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
	public Point showPlace(int nr) {		//znalezienie miejsca dla kafelaka nr...
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
	public void makeMove(char goal) {		//przesuniecie kafelka
		switch(goal) {
		case 'L':
			if((point.y)==0) return;
			board[point.x][point.y]=board[point.x][point.y-1];
			board[point.x][point.y-1]=0;
			point.y=point.y-1;			
			break;
		case 'R':
			if((point.y+1)==n) return;
			board[point.x][point.y]=board[point.x][point.y+1];
			board[point.x][point.y+1]=0;
			point.y=point.y+1;
			break;
		case 'U':
			if((point.x)==0) return;
			board[point.x][point.y]=board[point.x-1][point.y];
			board[point.x-1][point.y]=0;
			point.x=point.x-1;		
			break;
		case 'D':
			if((point.x+1)==n) return;
			board[point.x][point.y]=board[point.x+1][point.y];
			board[point.x+1][point.y]=0;
			point.x=point.x+1;				
			break;
		}	
	}
	public void mixPuzzle(int many) {		//pomieszanie kafelkow
		Random fate;
		int a;
		fate =new Random();
		for(int i=0;i<many;i++) {
			a=fate.nextInt(4);
			if(a==0) makeMove('L');
			if(a==1) makeMove('R');
			if(a==2) makeMove('U');
			if(a==3) makeMove('D');
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
	public static void main(String[] args) {
		SlidingPuzzle sp= new SlidingPuzzle();
		
		System.out.println(sp.toString());
		sp.makeMove('R');
		System.out.print(sp.toString());
		System.out.println("x="+sp.point.x+"  y="+sp.point.y+"\n");
		
		sp.makeMove('D');
		System.out.print(sp.toString());
		System.out.println("x="+sp.point.x+"  y="+sp.point.y+"\n");	
		
		sp.makeMove('L');
		System.out.print(sp.toString());
		System.out.println("x="+sp.point.x+"  y="+sp.point.y+"\n");
		
		sp.makeMove('U');
		System.out.print(sp.toString());
		System.out.println("x="+sp.point.x+"  y="+sp.point.y+"\n");	
		
		sp.mixPuzzle(100);
		System.out.print(sp.toString());
		System.out.println("x="+sp.point.x+"  y="+sp.point.y+"\n");
		
		System.out.println("dla 7: x="+sp.showPlace(7).x+"  y="+sp.showPlace(7).y);
		System.out.println("Manhatan: "+sp.hManhatan());
		System.out.println("MissTiles: "+sp.hMissTiles());

	}
}
