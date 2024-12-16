public class User {
    String name ;
    int levelpased = 0 ;
    int highestScore = 0 ;
    public User(String name){
        this.name = name ;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return (this.name != null && other.name != null)
                    && this.name.equals(other.name);
        }
        return false;
    }

}
