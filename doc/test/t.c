#ifdef __GNUC__
#include <stdio.h>
#endif
void function(int a, int b, int c) {
    printf("%d %d %d\n", a, b, c);
}

int i1() {
    printf("i1\n");
    return 0;
}

int i2() {
    printf("i2\n");
    return 0;
}

int i3() {
    printf("i3\n");
    return 0;
}

void i(int i1, int i2, int i3) {
}

int main() {
    int i = 0,a,b,c;
    function(a=++i, b=i, c=i++);
//    function(++i, i, i++);
    function(a, b, c);

	int k=0, a$a;
	a$a=100;
	k=k++ + ++k;
	printf("k=%d,%d\n", k, a$a);
	k+=++k;
	printf("k=%d\n", k);
	k=-k;
	printf("k=%d\n", k);
	k%=2;
	k^=2;
	k&=2;
	k<<=2;
	printf("k=%d\n", k);
	return k;
}