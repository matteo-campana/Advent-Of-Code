package io.github.matteocampana.adventofcode.puzzles.day17;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day17/test.txt");
        List<String> input = new ArrayList<>();

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    input.add(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int registerA = Integer.parseInt(input.get(0).split(":")[1].trim());
        int registerB = Integer.parseInt(input.get(1).split(":")[1].trim());
        int registerC = Integer.parseInt(input.get(2).split(":")[1].trim());
        int[] program = Arrays.stream(input.get(4).split(":")[1].trim().split(",")).mapToInt(Integer::parseInt)
                .toArray();

        ChronospatialComputer chronospatialCompute = new ChronospatialComputer(registerA, registerB, registerC);
        String programResult = chronospatialCompute.executeProgram(program);

        System.out.println("[PART 1] The program output is: " + programResult);
    }



    private static class ChronospatialComputer {

        int registerA;
        int registerB;
        int registerC;

        public ChronospatialComputer(int registerA, int registerB, int registerC) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
        }

        public String toString() {
            return "ChronospatialComputer{" +
                    "registerA=" + registerA +
                    ", registerB=" + registerB +
                    ", registerC=" + registerC +
                    '}';
        }




        // find a new value to which you can initialize register A so that the program's
        // output instructions produce an exact copy of the program itself.
        public int findMinRegisterAValue(int[] program) {
            int registerA = 0;
            while (true) {
                reset(registerA);
                String programOutput = executeProgram(program);
                int[] output = Arrays.stream(programOutput.split(",")).mapToInt(Integer::parseInt).toArray();
                if (matchesProgram(output, program)) {
                    return registerA;
                }
                registerA++;
            }
        }

        private boolean matchesProgram(int[] output, int[] expected) {
            if (output.length != expected.length) {
                return false;
            }
            for (int i = 0; i < output.length; i++) {
                if (output[i] != expected[i]) {
                    return false;
                }
            }
            return true;
        }

        private void reset(int registerA) {
            this.registerA = registerA;
            this.registerB = 0;
            this.registerC = 0;
        }

        // the program 0,1,2,3 would run the instruction whose opcode is 0 and pass it
        // the operand 1, then run the instruction having opcode 2 and pass it the
        // operand 3, then halt.
        public String executeProgram(int[] program) {
            StringBuilder programOutput = new StringBuilder();
            int instructionPointer = 0;
            while (instructionPointer < program.length) {
                int opcode = program[instructionPointer];
                int operand = program[instructionPointer + 1];
                boolean jumped = false;
                switch (opcode) {
                    case 0:
                        adv(operand);
                        break;
                    case 1:
                        bxl(operand);
                        break;
                    case 2:
                        bst(operand);
                        break;
                    case 3:
                        int jump = jnz(operand);
                        if (jump != -1) {
                            instructionPointer = jump;
                            jumped = true;
                        } else {
                            instructionPointer += 2;
                        }
                        break;
                    case 4:
                        bxc(operand);
                        break;
                    case 5:
                        programOutput.append(out(operand)).append(",");
                        break;
                    case 6:
                        bdv(operand);
                        break;
                    case 7:
                        cdv(operand);
                        break;
                    default:
                        break;
                }
                if (!jumped)
                    instructionPointer += 2;

            }
            return programOutput.substring(0, programOutput.length() - 1);
        }

        // Helper method to get the value of a combo operand
        private int getComboOperandValue(int operand) {
            return switch (operand) {
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> operand;
            };
        }

        // The adv instruction (opcode 0) performs division. The numerator is the value
        // in the A register. The denominator is found by raising 2 to the power of the
        // instruction's combo operand. (So, an operand of 2 would divide A by 4 (2^2);
        // an operand of 5 would divide A by 2^B.) The result of the division operation
        // is truncated to an integer and then written to the A register.

        private void adv(int operand) {
            int value = getComboOperandValue(operand);
            registerA /= (int) Math.pow(2, value);
        }

        // The bxl instruction (opcode 1) calculates the bitwise XOR of register B and
        // the instruction's literal operand, then stores the result in register B.

        private void bxl(int operand) {
            registerB ^= operand;
        }

        // The bst instruction (opcode 2) calculates the value of its combo operand
        // modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to
        // the B register.

        private void bst(int operand) {
            int value = getComboOperandValue(operand);
            registerB = value % 8;
        }

        // The jnz instruction (opcode 3) does nothing if the A register is 0. However,
        // if the A register is not zero, it jumps by setting the instruction pointer to
        // the value of its literal operand; if this instruction jumps, the instruction
        // pointer is not increased by 2 after this instruction.

        private int jnz(int operand) {
            if (registerA != 0) {
                // jump
                return operand;
            }
            return -1;
        }

        // The bxc instruction (opcode 4) calculates the bitwise XOR of register B and
        // register C, then stores the result in register B. (For legacy reasons, this
        // instruction reads an operand but ignores it.)

        private void bxc(int operand) {
            registerB ^= registerC;
        }

        // The out instruction (opcode 5) calculates the value of its combo operand
        // modulo 8, then outputs that value. (If a program outputs multiple values,
        // they are separated by commas.)

        private String out(int operand) {
            int value = getComboOperandValue(operand);
            return String.valueOf(value % 8);
        }

        // The bdv instruction (opcode 6) works exactly like the adv instruction except
        // that the result is stored in the B register. (The numerator is still read
        // from the A register.)

        private void bdv(int operand) {
            int value = getComboOperandValue(operand);
            registerB = (int) (registerA / Math.pow(2, value));
        }

        // The cdv instruction (opcode 7) works exactly like the adv instruction except
        // that the result is stored in the C register. (The numerator is still read
        // from the A register.)

        private void cdv(int operand) {
            int value = getComboOperandValue(operand);
            registerC = (int) (registerA / Math.pow(2, value));
        }
    }
}
