from asyncore import write
from neo4j import GraphDatabase;

class HelloWorldExample:
    def __init__(self, uri, user, password):
        self.driver = GraphDatabase.driver(uri, auth=(user, password))

    def close(self):
        self.driver.close()

    def nodes(self):
        with self.driver.session() as session:
            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MERGE (player:Player {name: Row.Player}) SET  player.number=Row.`Squad Number`, player.position=Row.Position, player.birthday=Row.`Date of Birth`, player.age=Row.Age, player.caps=Row.Caps, player.goals=Row.Goals")

            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MERGE (country:Country {name: Row.Team}) SET  country.group=Row.Group"
                )

            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MERGE (club:Club {name: Row.Club})"
                )

    def relations(self):
        with self.driver.session() as session:
            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MATCH(player:Player {name: Row.Player}),(country:Country {name: Row.Team}) CREATE (player)-[:From]->(country)"
                )

            session.run(
                "LOAD CSV WITH HEADERS FROM 'file:///WorldCup.csv' AS Row MATCH(player:Player {name: Row.Player}),(club:Club {name: Row.Club}) CREATE (player)-[:Plays_At]->(club)"
            )

    def result(self, query):
        with self.driver.session() as session:
            return list(session.run(query))


if __name__ == "__main__":
    greeter = HelloWorldExample("bolt://localhost:7687", "neo4j", "dani")
    
    #greeter.nodes()
    #greeter.relations()

    with open("CBD_L44c_output.txt", "w") as writer:
        writer.write("//NMEC: 98498")
        writer.write("#1 - Get all captains for every nation.\n")
        writer.write("match (n:Player)-[:From]-(country:Country) where n.name contains \"captain\" return distinct n.name, country.name\n")
        for i in greeter.result("match (n:Player)-[:From]-(country:Country) where n.name contains \"captain\" return distinct n.name, country.name "):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")

        writer.write("\n#2 - Get the best goalscorer.\n")
        writer.write("match (player:Player) where (player.name is not null) and (player.goals is not null) return player.name, player.goals order by toInteger(player.goals) desc limit 1\n")
        for i in greeter.result("match (player:Player) where (player.name is not null) and (player.goals is not null) return player.name, player.goals order by toInteger(player.goals) desc limit 1 "):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")

        writer.write("\n#3 - Get all players for Real Madrid.\n")
        writer.write("match (player:Player)-[:Plays_At]-(club:Club) where club.name=\"Real Madrid\" return distinct player.name\n")
        for i in greeter.result("match (player:Player)-[:Plays_At]-(club:Club) where club.name=\"Real Madrid\" return distinct player.name"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")

        writer.write("\n#4 - Get all clubs that are representated.\n")
        writer.write("match (club:Club) return distinct club.name\n")
        for i in greeter.result("match (club:Club) return distinct club.name"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")
            
        writer.write("\n#5 - Get goalscorers with over than 35 goals ordered by number of goals(ascendent).\n")
        writer.write("match (player: Player) where toInteger(player.goals) > 35 return player.name, player.goals order by player.goals\n")
        for i in greeter.result("match (player: Player) where toInteger(player.goals) > 35 return player.name, player.goals order by player.goals"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")
          
        writer.write("\n#6 - Get all the teams in group B\n")
        writer.write("match (country:Country {group : 'B'}) return country.name, country.group\n")
        for i in greeter.result("match (country:Country {group : 'B'}) return country.name, country.group"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")
          
        writer.write("\n#7 - Get all portuguese players.\n")
        writer.write("match (player:Player)-[:From]->(country:Country) where country.name=\"Portugal\" return distinct player.name\n")
        for i in greeter.result("match (player:Player)-[:From]->(country:Country) where country.name=\"Portugal\" return distinct player.name"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")
          
        writer.write("\n#8 - Get all midfielders younger than 20.\n")
        writer.write("match (p: Player {position : \"MF\"} ) WHERE toInteger(p.age) < 20 RETURN p.name\n")
        for i in greeter.result("match (p: Player {position : \"MF\"} ) WHERE toInteger(p.age) < 20 RETURN p.name"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")
          
        writer.write("\n#9 - Get all players that plays for the same nation and club.\n")
        writer.write("match (p1:Player)-[:From]->(c:Country)<-[:From]-(p2:Player) where id(p1) < id(p2) with p1 , p2 match (p1)-[:Plays_At]->(club:Club)<-[:Plays_At]-(p2) return distinct p1.name, p2.name\n")
        for i in greeter.result("match (p1:Player)-[:From]->(c:Country)<-[:From]-(p2:Player) where id(p1) < id(p2) with p1 , p2 match (p1)-[:Plays_At]->(club:Club)<-[:Plays_At]-(p2) return distinct p1.name, p2.name"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")
          
        writer.write("\n#10 - Get nation goals.\n")
        writer.write("match (player:Player)-[:From]-(country:Country) return sum(toInteger(player.goals)) , country.name\n")
        for i in greeter.result("match (player:Player)-[:From]-(country:Country) return sum(toInteger(player.goals)) , country.name"):
            s=""
            for j in range(len(i.items())):
                s += str(i.items()[j][1]) + ", "
            writer.write("  " + str(s) + "\n")
          

        