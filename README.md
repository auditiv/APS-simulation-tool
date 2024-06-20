APS Simulation Tool

This Springboot project implements a full simulation suite for Artificial Pancreas Systems.
The whole logic of the web app is inside the folder src/main/kotlin/com.example.APS_simulation_tool
The web app runs on localhost on port :8090
All the components needed for the simulation are inside the folder src/.../com.example.APS_simulation_tool/components.
I hope the rest is clear by the package names.

HTML-sites:
All html files are inside the folder src/.../resources/templates .
When the names begin with "f-" (for fragment) they are being imported into another html file.

APS-Simulation:
As Patient I used the UVA/Padova (2014) Meal Model to simulate the blood sugar of a Diabetes Type 1  Patient
The code has been influenced od translated and simplified where possible by my from the following pip package:
Jinyu Xie. Simglucose v0.2.1 (2018) [Online]. Available: https://github.com/jxx123/simglucose. Accessed on: 03-01-2024.

Thanks for the valuable descriptions and explanations!

