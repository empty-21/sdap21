package shinhands.com.services;

// import org.infinispan.protostream.annotations.ProtoFactory;
// import org.infinispan.protostream.annotations.ProtoField;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bucket {

    // @ProtoField(number = 1)
    // String Key;

    // @ProtoField(number = 2)
    // String Name;

    // @ProtoField(number = 3)
    // Object Value;

    // // Used with Json serialization
    // public Bucket() {
    // }

    // @ProtoFactory
    // public Bucket(String Key, String Name, Object Value) {
    //     if(Key == null || Key.equals(""))
    //         throw new IllegalArgumentException("Key cannot be null");
    //     else {
    //         this.Key = Key;
    //         this.Name = Name;
    //         this.Value = Value;
    //     }
    // }

    // public Bucket(String Key, Object Value) {
    //     if(Key == null || Key.equals(""))
    //         throw new IllegalArgumentException("Key cannot be null");
    //     else {
    //         this.Key = Key;
    //         this.Value = Value;
    //     }
    // }

}