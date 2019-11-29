package com.adonis.nov29app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etID, etFname, etLname;//etSection;
    Spinner sSection;
    FirebaseDatabase db;
    DatabaseReference refStud;
    List<Student> listStud;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etID = findViewById(R.id.etid);
        etFname = findViewById(R.id.etfname);
        etLname = findViewById(R.id.etlname);
        sSection = findViewById(R.id.spinner);

        db = FirebaseDatabase.getInstance();
        refStud = db.getReference("students");
        //refStud.setValue("Hello");

        listStud = new ArrayList<>();
        i = 0;

    }

    @Override
    protected void onStart() {
        super.onStart();
        refStud.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listStud.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Student stud = ds.getValue(Student.class);
                    listStud.add(stud);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addRecord (View v) {
        //String id = etID.getText().toString();
        String id = refStud.push().getKey();
        String fname = etFname.getText().toString();
        String lname = etLname.getText().toString();
        //String section = etSection.getText().toString();
        String section = sSection.getSelectedItem().toString();

        Student stud = new Student(id, fname, lname, section);
        //refStud.setValue(stud);
        refStud.child(id).setValue(stud);

        Toast.makeText(this, "Saved in Firebase", Toast.LENGTH_LONG).show();
    }

    public void moveFirst (View v) {
        Student stud = listStud.get(0);
        etID.setText(stud.getId());
        etFname.setText(stud.getFname());
        etLname.setText(stud.getLname());
        //sSection.setText(stud.getSection());
    }

    public void moveNext (View v) {
        Student stud;

        if (i == listStud.size()-1) {
            stud = listStud.get(listStud.size()-1);
            Toast.makeText(this, "Last Record.", Toast.LENGTH_LONG).show();
        }

        else {
            stud = listStud.get(++i);
        }

        etID.setText(stud.getId());
        etFname.setText(stud.getFname());
        etLname.setText(stud.getLname());
    }

    public void editRecord (View v) {
        String id = etID.getText().toString();
        String fname = etFname.getText().toString();
        String lname = etLname.getText().toString();
        String section = sSection.getSelectedItem().toString();

        Student stud = new Student(id, fname, lname, section);
        refStud.child(id).setValue(stud);

        Toast.makeText(this, "Record Updated.", Toast.LENGTH_LONG).show();
    }

    public void deleteRecord (View v) {
        String id = etID.getText().toString();
        refStud.child(id).removeValue();
        Toast.makeText(this, "Record Deleted.", Toast.LENGTH_LONG).show();
    }

    public void movePrevious (View v) {
        Student stud;

        if (i == 0) {
            stud = listStud.get(0);
            Toast.makeText(this, "First Record.", Toast.LENGTH_LONG).show();
        }

        else {
            stud = listStud.get(--i);
        }

        etID.setText(stud.getId());
        etFname.setText(stud.getFname());
        etLname.setText(stud.getLname());
    }

    public void moveLast (View v) {
        Student stud = listStud.get(listStud.size()-1);

        etID.setText(stud.getId());
        etFname.setText(stud.getFname());
        etLname.setText(stud.getLname());

        i = listStud.size()-1;
    }
}
