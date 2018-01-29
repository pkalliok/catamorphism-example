
data Expr = Ap Expr Expr | K | S | I | B | C | W | Var String deriving(Show,Eq)

rewrite (Ap (Ap K x) y) = x
rewrite (Ap (Ap (Ap S x) y) z) = Ap (Ap x z) (Ap y z)
rewrite (Ap I x) = x
rewrite (Ap (Ap (Ap B x) y) z) = Ap x (Ap y z)
rewrite (Ap (Ap (Ap C x) y) z) = Ap (Ap x z) y
rewrite (Ap (Ap W x) y) = Ap (Ap x y) y
rewrite (Ap x y) = Ap (rewrite x) y
rewrite x = x

norm = takeWhile (\ term -> term /= rewrite term) . iterate rewrite

